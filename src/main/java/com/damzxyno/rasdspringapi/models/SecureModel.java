package com.damzxyno.rasdspringapi.models;

import java.util.HashSet;
import java.util.Set;

public class SecureModel {
    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();

    public void addRole (String role){
        roles.add(role);
    }

    public void setRoles(Set<String> roles){
        this.roles = roles;
    }

    public void addPermission (String permission){
        permissions.add(permission);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }


}
