package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.utils.RASDSpecification;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.damzxyno.rasdspringapi.models.RASD;
import com.damzxyno.rasdspringapi.utils.RASDSpecBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
@Service
public class ReportingService implements ApplicationContextAware {
    private Logger logger = Logger.getLogger(ReportingService.class.getName());

    private final ControllerClassDocProvider controllerDocProvider;
    private final ConfigurationClassDocProvider configDocProvider;
    private final ProvisionalClassDocProvider provisionalDocProvider;
    private final SecurityMapperService securityMapperService;
    private final ObjectMapper objectMapper;
    private ApplicationContext context;
    private RASD rasdCache;


    public ReportingService(
            RequestMappingHandlerMapping requestMappingHandlerMapping){
        this.securityMapperService = new SecurityMapperService();
        controllerDocProvider = new ControllerClassDocProvider(requestMappingHandlerMapping, securityMapperService);
        configDocProvider = new ConfigurationClassDocProvider(securityMapperService);
        provisionalDocProvider = new ProvisionalClassDocProvider(requestMappingHandlerMapping, securityMapperService);
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    public String getAuthorisationReport(HttpServletRequest request) throws JsonProcessingException{
        RASDSpecification spec = getRASDSPecification();
        RASD response = null;
        if (spec.isUseCache() && rasdCache != null){
            response = rasdCache;
        } else {
            response = extractSecurityConfig(request, spec);
                if (spec.isUseCache()){
                    rasdCache = response;
                }
        }

        return objectMapper.writeValueAsString(response);
    }

    private RASD extractSecurityConfig(HttpServletRequest request, RASDSpecification spec){
        controllerDocProvider.inspectRequestMappings(spec);
        configDocProvider.inspectRequestMappings();
        provisionalDocProvider.inspectRequestMappings(spec);
        return securityMapperService.getSecurityDocumentation();
    }

    private RASDSpecification getRASDSPecification() {
        try {
            RASDSpecification spec = context.getBean(RASDSpecification.class);
            return spec;
        } catch (Exception ex){
        }
        return RASDSpecBuilder.getINSTANCE()
                .useCache()
                .hideSpringInternals()
                .hideOpenApiInternals()
                .build();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.configDocProvider.setApplicationContext(context);
        this.provisionalDocProvider.setApplicationContext(context);
    }
}
