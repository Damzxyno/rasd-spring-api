package com.damzxyno.rasdspringapi.models;

import java.util.Set;

public class RASDSpecBuilder {
    private static RASDSpecBuilder INSTANCE;
    private RASDSpecification spec;
    private RASDSpecBuilder() {
        spec = new RASDSpecification();
    };

    public static RASDSpecBuilder getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new RASDSpecBuilder();

        }
        return INSTANCE;
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
}
