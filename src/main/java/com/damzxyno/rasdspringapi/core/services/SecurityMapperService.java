package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.models.*;


import java.util.List;

public class SecurityMapperService {
    private final RASD securityDocumentation;
    public SecurityMapperService(RASD securityDocumentation){
        this.securityDocumentation = securityDocumentation;
    }

    public RASD getSecurityDocumentation(){
        return this.securityDocumentation;
    }
    public void addStaticSingleRole (String role, Operation operation){
        if (operation.isReadOnly()){
            return;
        }
        operation.getAuthorisationMod().getStaticMod().addRole(role);
    }
    public void addStaticSinglePermission (String permission, Operation operation){
        if (operation.isReadOnly()){
            return;
        }
        operation.getAuthorisationMod().getStaticMod().addPermission(permission);
    }

    public void setOperationHasAuthority(String authority, PathItem pathItem) {
        if (pathItem.getGet() != null) {
            addStaticSinglePermission(authority, pathItem.getGet());
        }
        if (pathItem.getPut() != null) {
            addStaticSinglePermission(authority,pathItem.getPut());
        }
        if (pathItem.getPost() != null) {
            addStaticSinglePermission(authority,pathItem.getPost());
        }
        if (pathItem.getDelete() != null) {
            addStaticSinglePermission(authority,pathItem.getDelete());
        }
        if (pathItem.getOptions() != null) {
            addStaticSinglePermission(authority,pathItem.getOptions());
        }
        if (pathItem.getHead() != null) {
            addStaticSinglePermission(authority,pathItem.getHead());
        }
        if (pathItem.getPatch() != null) {
            addStaticSinglePermission(authority,pathItem.getPatch());
        }
        if (pathItem.getTrace() != null) {
            addStaticSinglePermission(authority,pathItem.getTrace());
        }
        securityDocumentation.getMetaData().getPermissions().add(authority);
    }

    public void addMultipleRoles(List<String> roles, List<SecureModel> secureModels) {
        for (String role : roles) {
            boolean roleExists = false;

            // Check if the role already exists in any SecureModel
            for (SecureModel model : secureModels) {
                if (model.getRoles().contains(role)) {
                    roleExists = true;
                    break;
                }
            }

            // If the role doesn't exist, add it to a new SecureModel and add to the list
            if (!roleExists) {
                SecureModel secureModel = new SecureModel();
                secureModel.getRoles().add(role);
                secureModels.add(secureModel);
            }
        }
    }

    public void addMultiplePermissions(List<String> authorities, List<SecureModel> secureModels) {
        for (String authority : authorities) {
            boolean authorityExists = false;

            // Check if the authority already exists in any SecureModel
            for (SecureModel model : secureModels) {
                if (model.getPermissions().contains(authority)) {
                    authorityExists = true;
                    break;
                }
            }

            // If the authority doesn't exist, add it to a new SecureModel and add to the list
            if (!authorityExists) {
                SecureModel secureModel = new SecureModel();
                secureModel.getPermissions().add(authority);
                secureModels.add(secureModel);
            }
        }
    }





    public void setOperationHasRole(String role, PathItem pathItem) {
        if (pathItem.getGet() != null) {
            addStaticSingleRole(role, pathItem.getGet());
        }
        if (pathItem.getPut() != null) {
            addStaticSingleRole(role,pathItem.getPut());
        }
        if (pathItem.getPost() != null) {
            addStaticSingleRole(role,pathItem.getPost());
        }
        if (pathItem.getDelete() != null) {
            addStaticSingleRole(role,pathItem.getDelete());
        }
        if (pathItem.getOptions() != null) {
            addStaticSingleRole(role,pathItem.getOptions());
        }
        if (pathItem.getHead() != null) {
            addStaticSingleRole(role,pathItem.getHead());
        }
        if (pathItem.getPatch() != null) {
            addStaticSingleRole(role,pathItem.getPatch());
        }
        if (pathItem.getTrace() != null) {
            addStaticSingleRole(role,pathItem.getTrace());
        }
        securityDocumentation.getMetaData().getRoles().add(role);
    }

    public void setOperationMultipleRoles(List<String> roles, Operation operation){
        if (!operation.isReadOnly()){
            addMultipleRoles(roles, operation.getAuthorisationMod().getRelativeMod());
        }
    }
    public void setOperationMultipleRoles(List<String> roles, PathItem pathItem) {
        if (pathItem.getGet() != null) {
            setOperationMultipleRoles(roles, pathItem.getGet());
        }
        if (pathItem.getPut() != null) {
            setOperationMultipleRoles(roles, pathItem.getPut());
        }
        if (pathItem.getPost() != null) {
            setOperationMultipleRoles(roles, pathItem.getPost());
        }
        if (pathItem.getDelete() != null) {
            setOperationMultipleRoles(roles, pathItem.getDelete());
        }
        if (pathItem.getOptions() != null) {
            setOperationMultipleRoles(roles, pathItem.getOptions());
        }
        if (pathItem.getHead() != null) {
            setOperationMultipleRoles(roles, pathItem.getHead());
        }
        if (pathItem.getPatch() != null) {
            setOperationMultipleRoles(roles, pathItem.getPatch());
        }
        if (pathItem.getTrace() != null) {
            setOperationMultipleRoles(roles, pathItem.getTrace());
        }
        securityDocumentation.getMetaData().getRoles().addAll(roles);
    }

    private void setOperationMultipleAuthorities(List<String> authorities, Operation operation){
        if (!operation.isReadOnly()){
            addMultiplePermissions(authorities, operation.getAuthorisationMod().getRelativeMod());
        }
    }
    public void setOperationMultipleAuthorities(List<String> authorities, PathItem pathItem) {
        if (pathItem.getGet() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getGet());
        }
        if (pathItem.getPut() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getPut());
        }
        if (pathItem.getPost() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getPost());
        }
        if (pathItem.getDelete() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getDelete());
        }
        if (pathItem.getOptions() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getOptions());
        }
        if (pathItem.getHead() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getHead());
        }
        if (pathItem.getPatch() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getPatch());
        }
        if (pathItem.getTrace() != null) {
            setOperationMultipleAuthorities(authorities, pathItem.getTrace());
        }
        securityDocumentation.getMetaData().getPermissions().addAll(authorities);
    }

    public void addFreePermission(Operation operation){
        Authorisation authorisation = operation.getAuthorisationMod();
        if (authorisation.getStaticMod().getPermissions().isEmpty() &&
            authorisation.getStaticMod().getRoles().isEmpty() &&
            authorisation.getRelativeMod().isEmpty()){
            operation.setAuthenticated(false);
            operation.setReadOnly();
        }
    }
    public void setPermissionFree(PathItem pathItem) {
        if (pathItem.getGet() != null) {
            addFreePermission(pathItem.getGet());
        }
        if (pathItem.getPut() != null) {
            addFreePermission(pathItem.getPut());
        }
        if (pathItem.getPost() != null) {
            addFreePermission(pathItem.getPost());
        }
        if (pathItem.getDelete() != null) {
            addFreePermission(pathItem.getDelete());
        }
        if (pathItem.getOptions() != null) {
            addFreePermission(pathItem.getOptions());
        }
        if (pathItem.getHead() != null) {
            addFreePermission(pathItem.getHead());
        }
        if (pathItem.getPatch() != null) {
            addFreePermission(pathItem.getPatch());
        }
        if (pathItem.getTrace() != null) {
            addFreePermission(pathItem.getTrace());
        }
    }
}
