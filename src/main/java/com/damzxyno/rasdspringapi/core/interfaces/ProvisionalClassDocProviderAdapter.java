package com.damzxyno.rasdspringapi.core.interfaces;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public interface ProvisionalClassDocProviderAdapter {
    void configure(RequestMappingHandlerMapping handlerMapping, SecurityMapperProxy securityMapper);
}
