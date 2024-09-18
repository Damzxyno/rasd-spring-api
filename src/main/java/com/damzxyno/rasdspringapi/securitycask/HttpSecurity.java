package com.damzxyno.rasdspringapi.securitycask;

import com.damzxyno.rasdspringapi.core.services.SecurityMapperService;

public class HttpSecurity extends AbstractSecurityBuilder<SecurityFilterChain>{
    private CSRFConfigurer csrfConfigurer;
    private AntMatchingConfigurer antMatchingConfigurer;
    private final SecurityMapperService mapperService;

    public HttpSecurity(SecurityMapperService mapperService){
        super(new SecurityFilterChain());
        this.mapperService = mapperService;
    }

    public SecurityMapperService getSecurityMapperService(){
        return this.mapperService;
    }

    public CSRFConfigurer csrf(){
        if (csrfConfigurer == null){
            csrfConfigurer = new CSRFConfigurer(this);
        }
        return csrfConfigurer;
    };

    public AntMatchingConfigurer authorizeHttpRequests (){
        if (antMatchingConfigurer == null){
            antMatchingConfigurer = new AntMatchingConfigurer(this);
        }
        return antMatchingConfigurer;
    }

    public HttpSecurity sessionManagement(){
        return this;
    }
    public HttpSecurity sessionCreationPolicy(Object o){
        return this;
    }
    public HttpSecurity and(){
        return this;
    }

    public HttpSecurity authenticationProvider(Object o){
        return this;
    }
    public HttpSecurity addFilterBefore(Object o, Class<?> clazz){
        return this;
    }

}
