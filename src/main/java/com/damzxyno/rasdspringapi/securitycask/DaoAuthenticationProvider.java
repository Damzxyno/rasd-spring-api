package com.damzxyno.rasdspringapi.securitycask;

public class DaoAuthenticationProvider extends AuthenticationProvider {

    public DaoAuthenticationProvider setPasswordEncoder(Object object){
        return this;
    }

    public DaoAuthenticationProvider setUserDetailsService(Object object){
        return this;
    }
}
