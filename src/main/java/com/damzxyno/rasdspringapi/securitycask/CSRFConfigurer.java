package com.damzxyno.rasdspringapi.securitycask;

import com.damzxyno.rasdspringapi.core.services.SecurityMapperService;

public class CSRFConfigurer{
    private final HttpSecurity httpSecurity;

    public CSRFConfigurer(HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
    }


    public HttpSecurity disable(){
        return httpSecurity;
    }
}