package com.damzxyno.rasdspringapi.utils;

import java.util.Set;

public class RASDSpecBuilder {
    private RASDSpecification spec;
    private RASDSpecBuilder() {
        spec = new RASDSpecification();
    };

    public static RASDSpecBuilder getINSTANCE(){
        return new RASDSpecBuilder();
    }


    public RASDSpecBuilder hideOpenApiInternals(){
        spec.setHideOpenApiInternals(true);
        return this;
    }
    public RASDSpecBuilder hideSpringInternals(){
//        spec.setHideSpringInternals(true);
        spec.getExcludedPackages().addAll(
                Set.of(
                        "org.springframework",
                        "org.springframework.boot",
                        "org.springframework.data",
                        "org.springframework.security")
        );
        return this;

    }

    public RASDSpecification build(){
        return this.spec;
    }

    public RASDSpecBuilder useCache() {
        spec.setUseCache(true);
        return this;
    }
}
