package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.core.interfaces.ProvisionalClassDocProviderAdapter;
import com.damzxyno.rasdspringapi.utils.RASDSpecification;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.Map;

public class ProvisionalClassDocProvider {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SecurityMapperService securityMapperService;
    private ApplicationContext applicationContext;
    public ProvisionalClassDocProvider(RequestMappingHandlerMapping requestMappingHandlerMapping, SecurityMapperService securityMapperService) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.securityMapperService = securityMapperService;
    }

    public void inspectRequestMappings(RASDSpecification spec) {
        AbstractApplicationContext abstractContext = (AbstractApplicationContext)applicationContext;
        Map<String, ProvisionalClassDocProviderAdapter> beans = applicationContext.getBeansOfType(ProvisionalClassDocProviderAdapter.class);
        beans.forEach((key, bean) -> {
           bean.configure(requestMappingHandlerMapping, securityMapperService);
        });
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
