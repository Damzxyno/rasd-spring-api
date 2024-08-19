package com.damzxyno.rasdspringapi.models;

import java.util.HashSet;
import java.util.Set;


public class MetaData {
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();


    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
