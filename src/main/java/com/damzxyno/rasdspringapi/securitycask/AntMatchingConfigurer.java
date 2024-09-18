package com.damzxyno.rasdspringapi.securitycask;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AntMatchingConfigurer {
    private final HttpSecurity httpSecurity;
    private String [] patterns;
    public AntMatchingConfigurer(HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
    }

    private AntMatchingConfigurer newInstance(){
        return new AntMatchingConfigurer(httpSecurity);
    }

    public AntMatchingConfigurer antMatchers(String... antPatterns){
        patterns = antPatterns;
        return this;
    }

    public AntMatchingConfigurer permitAll(){
        httpSecurity.getSecurityMapperService().permitAllPatterns(patterns);
        return this.newInstance();
    }

    public AntMatchingConfigurer hasRole(String role){
        httpSecurity.getSecurityMapperService().assignRoleToPatterns(patterns, role);
        return this.newInstance();
    }

    public AntMatchingConfigurer hasAnyRole(String... roles){
        httpSecurity.getSecurityMapperService().assignRoleArrayToPatterns(patterns, roles);
        return this.newInstance();
    }

    public AntMatchingConfigurer hasAuthority(String authority){
        httpSecurity.getSecurityMapperService().assignPermissionToPatterns(patterns, authority);
        return this.newInstance();
    }

    public AntMatchingConfigurer hasAnyAuthority(String... authorities){
        httpSecurity.getSecurityMapperService().assignPermissionArrayToPatterns(patterns, authorities);
        return this.newInstance();
    }

    public AntMatchingConfigurer authenticated(){
        httpSecurity.getSecurityMapperService().authenticatePattern(patterns);
        return this.newInstance();
    }
    public HttpSecurity and(){
        return httpSecurity;
    }
}
