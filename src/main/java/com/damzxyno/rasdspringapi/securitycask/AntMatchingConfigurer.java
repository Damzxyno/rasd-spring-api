package com.damzxyno.rasdspringapi.securitycask;

import com.damzxyno.rasdspringapi.core.services.SecurityMapperService;
import org.springframework.util.PathMatcher;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AntMatchingConfigurer {
    private final HttpSecurity httpSecurity;
    private final SecurityMapperService mapperService;
    private final PathMatcher pathMatcher;
    private String [] patterns;
    public AntMatchingConfigurer(HttpSecurity httpSecurity, PathMatcher pathMatcher, SecurityMapperService securityMapperService) {
        this.httpSecurity = httpSecurity;
        this.pathMatcher = pathMatcher;
        this.mapperService = securityMapperService;
    }


    public AntMatchingConfigurer antMatchers(String... antPatterns){
        patterns = antPatterns;
        return this;
    }

    public AntMatchingConfigurer permitAll(){
        Arrays.stream(patterns).forEach(pattern -> {
            mapperService.getSecurityDocumentation().getPaths().forEach((urlPath, pathitem) ->{
                if (pathMatcher.match(pattern, urlPath)){
                    mapperService.setPermissionFree(pathitem);
                }
            });
        });
        return this;
    }

    public AntMatchingConfigurer hasRole(String role){
        Arrays.stream(patterns).forEach(pattern -> {
            mapperService.getSecurityDocumentation().getPaths().forEach((urlPath, pathitem) ->{
                if (pathMatcher.match(pattern, urlPath)){
                    mapperService.setOperationHasRole(role, pathitem);
                }
            });
        });
        return this;
    }

    public AntMatchingConfigurer hasAnyRole(String... roles){
        List<String> roleList = Arrays.stream(roles).collect(Collectors.toList());
        Arrays.stream(patterns).forEach(pattern -> {
            mapperService.getSecurityDocumentation().getPaths().forEach((urlPath, pathitem) ->{
                if (pathMatcher.match(pattern, urlPath)){
                    mapperService.setOperationMultipleRoles(roleList, pathitem);
                }
            });
        });
        return this;
    }

    public AntMatchingConfigurer hasAuthority(String authority){
        Arrays.stream(patterns).forEach(pattern -> {
            mapperService.getSecurityDocumentation().getPaths().forEach((urlPath, pathitem) ->{
                if (pathMatcher.match(pattern, urlPath)){
                    mapperService.setOperationHasAuthority(authority, pathitem);
                }
            });
        });
        return this;
    }

    public AntMatchingConfigurer hasAnyAuthority(String... authorities){
        List<String> authoritiesList = Arrays.stream(authorities ).collect(Collectors.toList());
        Arrays.stream(patterns).forEach(pattern -> {
            mapperService.getSecurityDocumentation().getPaths().forEach((urlPath, pathitem) ->{
                if (pathMatcher.match(pattern, urlPath)){
                    mapperService.setOperationMultipleAuthorities(authoritiesList, pathitem);
                }
            });
        });
        return this;
    }

    public HttpSecurity and(){
        return httpSecurity;
    }
}
