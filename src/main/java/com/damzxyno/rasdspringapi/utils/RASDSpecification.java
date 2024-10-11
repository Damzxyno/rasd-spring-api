package com.damzxyno.rasdspringapi.utils;

import java.util.HashSet;
import java.util.Set;

public class RASDSpecification {
    private boolean hideSpringInternals;
    private boolean hideOpenApiInternals;
    private Set<String> excludedPackages = new HashSet<>();
    private boolean useCache;




    public boolean isHideSpringInternals() {
        return hideSpringInternals;
    }

    public void setHideSpringInternals(boolean hideSpringInternals) {
        this.hideSpringInternals = hideSpringInternals;
    }

    public boolean isHideOpenApiInternals() {
        return hideOpenApiInternals;
    }

    public void setHideOpenApiInternals(boolean hideOpenApiInternals) {
        this.hideOpenApiInternals = hideOpenApiInternals;
    }

    public Set<String> getExcludedPackages() {
        return excludedPackages;
    }

    public void setExcludedPackages(Set<String> excludedPackages) {
        this.excludedPackages = excludedPackages;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }
}
