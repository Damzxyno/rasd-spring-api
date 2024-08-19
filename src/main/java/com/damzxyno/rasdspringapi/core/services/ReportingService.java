package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.models.RASDSpecification;
import com.damzxyno.rasdspringapi.securitycask.HttpSecurity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.damzxyno.rasdspringapi.models.RASD;
import com.damzxyno.rasdspringapi.models.RASDSpecBuilder;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    private ControllerClassDocProvider controllerDocProvider;
    private ConfigurationClassDocProvider configDocProvider;
    private final ObjectMapper objectMapper;
    private ApplicationContext context;


    public ReportingService(
            RequestMappingHandlerMapping requestMappingHandlerMapping){
        controllerDocProvider = new ControllerClassDocProvider(requestMappingHandlerMapping);
        configDocProvider = new ConfigurationClassDocProvider();
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    public String getAuthorisationReport(HttpServletRequest request) {
        try {
            RASD RASD = extractSecurityConfig(request);
            return objectMapper.writeValueAsString(RASD);
        } catch (JsonProcessingException ex){
            logger.log(Level.WARNING, "", ex);
        }
        return null;
    }

    private RASD extractSecurityConfig(HttpServletRequest request){
        RASDSpecification spec = getRASDSPecification();
        RASD config = new RASD();
        SecurityMapperService mapperService = new SecurityMapperService(config);
        controllerDocProvider.inspectRequestMappings(mapperService, spec);
        configDocProvider.inspectRequestMappings(mapperService);
        return config;
    }

    private RASDSpecification getRASDSPecification() {
        try {
            RASDSpecification spec = context.getBean(RASDSpecification.class);
            return spec;
        } catch (Exception ex){
        }
        return RASDSpecBuilder.getINSTANCE()
                .hideSpringInternals()
                .hideOpenApiInternals()
                .build();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        this.configDocProvider.setApplicationContext(context);
    }
}
