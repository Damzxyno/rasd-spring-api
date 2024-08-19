package com.damzxyno.rasdspringapi.securitycask;

import com.damzxyno.rasdspringapi.core.services.SecurityMapperService;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class HttpSecurity extends AbstractSecurityBuilder<SecurityFilterChain>{
    private CSRFConfigurer csrfConfigurer;
    private AntMatchingConfigurer antMatchingConfigurer;
    private final SecurityMapperService mapperService;
    private PathMatcher pathMatcher;

    public HttpSecurity(SecurityMapperService mapperService){
        super(new SecurityFilterChain());
        this.mapperService = mapperService;
        this.pathMatcher =  new AntPathMatcher();
    }


    public CSRFConfigurer csrf(){
        if (csrfConfigurer == null){
            csrfConfigurer = new CSRFConfigurer(this, mapperService);
        }
        return csrfConfigurer;
    };

    public AntMatchingConfigurer authorizeHttpRequests (){
        if (antMatchingConfigurer == null){
            antMatchingConfigurer = new AntMatchingConfigurer(this, pathMatcher, mapperService);
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
