package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.core.interfaces.SecurityMapperProxy;
import com.damzxyno.rasdspringapi.models.*;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

public class SecurityMapperService implements SecurityMapperProxy {
    private final RASD securityDocumentation;
    private final PathMatcher pathMatcher;
    public SecurityMapperService(){
        this.securityDocumentation = new RASD();
        pathMatcher = new AntPathMatcher();
    }

    public RASD getSecurityDocumentation(){
        buildDocumentation();
        return this.securityDocumentation;
    }

    private void buildDocumentation() {
        securityDocumentation.getPaths().values().forEach(pathItem -> {
            processAuthorisation(pathItem.getGet());
            processAuthorisation(pathItem.getPut());
            processAuthorisation(pathItem.getPost());
            processAuthorisation(pathItem.getDelete());
            processAuthorisation(pathItem.getOptions());
            processAuthorisation(pathItem.getHead());
            processAuthorisation(pathItem.getPatch());
            processAuthorisation(pathItem.getTrace());
        });
    }

    private void processAuthorisation(Operation operation) {
        if (operation == null) return;

        AuthorisationMod authorisationMod = operation.getAuthorisationMod();
        SecureModel staticMod = authorisationMod.getStaticMod();

        // Add static permissions and roles
        securityDocumentation.getMetaData().getPermissions().addAll(staticMod.getPermissions());
        securityDocumentation.getMetaData().getRoles().addAll(staticMod.getRoles());

        // Add relative permissions and roles
        authorisationMod.getRelativeMod().forEach(secureModel -> {
            securityDocumentation.getMetaData().getPermissions().addAll(secureModel.getPermissions());
            securityDocumentation.getMetaData().getRoles().addAll(secureModel.getRoles());
        });
    }
    public void addStaticSingleRole (String role, Operation operation){
        if (!operation.isReadOnly()){
            operation.getAuthorisationMod().getStaticMod().addRole(role);
            operation.setAuthenticated(true);
        }
    }
    public void addStaticSinglePermission (String permission, Operation operation){
        if (!operation.isReadOnly()){
            operation.getAuthorisationMod().getStaticMod().addPermission(permission);
            operation.setAuthenticated(true);
        }
    }

    public void setOperationHasAuthority(String authority, PathItem pathItem) {
        processOperations(pathItem, operation -> addStaticSinglePermission(authority, operation));
    }

    public void setOperationHasRole(String role, PathItem pathItem) {
        processOperations(pathItem, operation -> addStaticSingleRole(role, operation));
    }

    public void setOperationMultipleRoles(List<String> roles, PathItem pathItem) {
        processOperations(pathItem, operation -> setOperationMultipleRoles(roles, operation));
    }

    public void setOperationMultipleAuthorities(List<String> authorities, PathItem pathItem) {
        processOperations(pathItem, operation -> setOperationMultipleAuthorities(authorities, operation));
    }

    private void setOperationMultipleRoles(List<String> roles, Operation operation) {
        if (!operation.isReadOnly()) {
            addMultipleRoles(roles, operation.getAuthorisationMod().getRelativeMod());
            operation.setAuthenticated(true);
        }
    }

    private void setOperationMultipleAuthorities(List<String> authorities, Operation operation) {
        if (!operation.isReadOnly()) {
            addMultiplePermissions(authorities, operation.getAuthorisationMod().getRelativeMod());
            operation.setAuthenticated(true);
        }
    }

    // Utility method to process all operations in a PathItem
    private void processOperations(PathItem pathItem, Consumer<Operation> operationAction) {
        if (pathItem.getGet() != null) operationAction.accept(pathItem.getGet());
        if (pathItem.getPut() != null) operationAction.accept(pathItem.getPut());
        if (pathItem.getPost() != null) operationAction.accept(pathItem.getPost());
        if (pathItem.getDelete() != null) operationAction.accept(pathItem.getDelete());
        if (pathItem.getOptions() != null) operationAction.accept(pathItem.getOptions());
        if (pathItem.getHead() != null) operationAction.accept(pathItem.getHead());
        if (pathItem.getPatch() != null) operationAction.accept(pathItem.getPatch());
        if (pathItem.getTrace() != null) operationAction.accept(pathItem.getTrace());
    }

    // Add multiple roles to SecureModels
    public void addMultipleRoles(List<String> roles, List<SecureModel> secureModels) {
        roles.forEach(role -> {
            if (secureModels.stream().noneMatch(model -> model.getRoles().contains(role))) {
                SecureModel secureModel = new SecureModel();
                secureModel.getRoles().add(role);
                secureModels.add(secureModel);
            }
        });
    }

    // Add multiple permissions to SecureModels
    public void addMultiplePermissions(List<String> authorities, List<SecureModel> secureModels) {
        authorities.forEach(authority -> {
            if (secureModels.stream().noneMatch(model -> model.getPermissions().contains(authority))) {
                SecureModel secureModel = new SecureModel();
                secureModel.getPermissions().add(authority);
                secureModels.add(secureModel);
            }
        });
    }

    // Permit all operations in a PathItem
    public void permitAllOperationInPathItem(PathItem pathItem) {
        processOperations(pathItem, this::permitOperation);
    }

    // Remove authorization if no roles/permissions are set
    private void permitOperation(Operation operation) {
        AuthorisationMod authorisation = operation.getAuthorisationMod();
        if (authorisation.getStaticMod().getPermissions().isEmpty()
                && authorisation.getStaticMod().getRoles().isEmpty()
                && authorisation.getRelativeMod().isEmpty()) {
            operation.setAuthenticated(false);
            operation.setReadOnly();
        }
    }

    // Handle patterns for setting permissions/roles
    public void permitAllPatterns(String[] patterns) {
        processPatterns(patterns, this::permitAllOperationInPathItem);
    }

    public void assignRoleToPatterns(String[] patterns, String role) {
        processPatterns(patterns, (pathItem) -> setOperationHasRole(role, pathItem));
    }

    public void assignRoleArrayToPatterns(String[] patterns, String[] roles) {
        List<String> roleList = Arrays.asList(roles);
        processPatterns(patterns, (pathItem) -> setOperationMultipleRoles(roleList, pathItem));
    }

    public void assignPermissionToPatterns(String[] patterns, String authority) {
        processPatterns(patterns, (pathItem) -> setOperationHasAuthority(authority, pathItem));
    }

    public void assignPermissionArrayToPatterns(String[] patterns, String[] authorities) {
        List<String> authoritiesList = Arrays.asList(authorities);
        processPatterns(patterns, (pathItem) -> setOperationMultipleAuthorities(authoritiesList, pathItem));
    }

    public void authenticatePattern(String[] patterns) {
        processPatterns(patterns, (pathItem) -> processOperations(pathItem, (operation -> {
//            if (!operation.isReadOnly()){
                operation.setAuthenticated(true);
//            }
        }) ));
    }

    // Process patterns and apply actions to matching paths
    private void processPatterns(String[] patterns, Consumer<PathItem> pathItemAction) {
        Arrays.stream(patterns).forEach(pattern -> {
            securityDocumentation.getPaths().forEach((urlPath, pathItem) -> {
                if (pathMatcher.match(pattern, urlPath)) {
                    pathItemAction.accept(pathItem);
                }
            });
        });
    }


    // Utility method to handle path items and HTTP methods
    public void addAuthorisationMod(String pattern, RequestMethod method, AuthorisationMod authorisationMod) {
        securityDocumentation.getPaths().computeIfAbsent(pattern, k -> new PathItem());
        addAuthorisationModToPathItemWithHttpMethod(pattern, method, authorisationMod);
    }

    public void addTagToPatternOperation(String pattern, RequestMethod method, String tag) {
        securityDocumentation.getPaths().computeIfAbsent(pattern, k -> new PathItem());
        PathItem pathItem = securityDocumentation.getPaths().get(pattern);
        processOperationByMethod(pathItem, method, operation -> operation.addTagsItem(tag));
    }

    private void addAuthorisationModToPathItemWithHttpMethod(String pattern, RequestMethod method, AuthorisationMod authorisationMod) {
        PathItem pathItem = securityDocumentation.getPaths().get(pattern);
        processOperationByMethod(pathItem, method, operation -> addAuthorisationModToOperation(operation, authorisationMod));
    }

    private void createOperationForPathItem(PathItem pathItem, RequestMethod method) {
        if (method == RequestMethod.POST && pathItem.getPost() == null) pathItem.setPost(new Operation());
        else if (method == RequestMethod.PUT && pathItem.getPut() == null) pathItem.setPut(new Operation());
        else if (method == RequestMethod.DELETE && pathItem.getDelete() == null) pathItem.setDelete(new Operation());
        else if (method == RequestMethod.PATCH && pathItem.getPatch() == null) pathItem.setPatch(new Operation());
        else if (method == RequestMethod.HEAD && pathItem.getHead() == null) pathItem.setHead(new Operation());
        else if (method == RequestMethod.OPTIONS && pathItem.getOptions() == null) pathItem.setOptions(new Operation());
        else if (method == RequestMethod.TRACE && pathItem.getTrace() == null) pathItem.setTrace(new Operation());
        else if (method == RequestMethod.GET && pathItem.getGet() == null) pathItem.setGet(new Operation());
    }

    private void processOperationByMethod(PathItem pathItem, RequestMethod method, Consumer<Operation> operationAction) {
        createOperationForPathItem(pathItem, method);
        switch (method) {
            case POST : operationAction.accept(pathItem.getPost());
            break;
            case PUT : operationAction.accept(pathItem.getPut());
            break;
            case DELETE : operationAction.accept(pathItem.getDelete());
            break;
            case PATCH : operationAction.accept(pathItem.getPatch());
            break;
            case HEAD : operationAction.accept(pathItem.getHead());
            break;
            case OPTIONS : operationAction.accept(pathItem.getOptions());
            break;
            case TRACE : operationAction.accept(pathItem.getTrace());
            break;
            default : operationAction.accept(pathItem.getGet());
        }
    }

    private void addAuthorisationModToOperation(Operation operation, AuthorisationMod authorisationMod) {
        if (!operation.isReadOnly()) {
            addMissingAuthorisationModFromBIntoA(operation.getAuthorisationMod(), authorisationMod);
            operation.setAuthenticated(true);
        }
    }

    public void addMissingAuthorisationModFromBIntoA(AuthorisationMod a, AuthorisationMod b) {
        a.getStaticMod().getRoles().addAll(b.getStaticMod().getRoles());
        a.getStaticMod().getPermissions().addAll(b.getStaticMod().getPermissions());
        addMissingSecureModelListBIntoSecureModelListA(a.getRelativeMod(), b.getRelativeMod());
    }

    private void addMissingSecureModelListBIntoSecureModelListA(List<SecureModel> secureModelListA, List<SecureModel> secureModelListB){
        for (SecureModel secureModelB : secureModelListB){
            boolean isPresent = false;
            for (SecureModel secureModelA : secureModelListA){
                if (secureModelA.equalsThisSecureModel(secureModelB)){
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent){
                secureModelListA.add(secureModelB);
            }
        }
    }

    @Override
    public void addLocationAccepted(String [] patterns, RequestMethod method, List<String> locations) {
        processPatterns(patterns, (pathItem) -> processOperationByMethod(pathItem, method, (operation) -> {
            operation.getAuthorisationMod().getStaticMod().addAcceptedLocations(locations);
        } ));
    }

    @Override
    public void addLocationRestricted(String [] patterns, RequestMethod method, List<String> restricted) {
        processPatterns(patterns, (pathItem) -> processOperationByMethod(pathItem, method, (operation) -> {
            operation.getAuthorisationMod().getStaticMod().addRestrictedLocations(restricted);
        } ));
    }

    @Override
    public void addTimeAccepted(String [] patterns, RequestMethod method, EnumMap<DayOfWeek, TimeRange> acceptedTime) {
        processPatterns(patterns, (pathItem) -> processOperationByMethod(pathItem, method, (operation) -> {
            operation.getAuthorisationMod().getStaticMod().addAcceptedTimeRanges(acceptedTime);
        } ));
    }

    @Override
    public void addTimeRestricted(String [] patterns, RequestMethod method, EnumMap<DayOfWeek, TimeRange> restrictedTime) {
        processPatterns(patterns, (pathItem) -> processOperationByMethod(pathItem, method, (operation) -> {
            operation.getAuthorisationMod().getStaticMod().addRestrictedTimeRanges(restrictedTime);
        } ));
    }


}
