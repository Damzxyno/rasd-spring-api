package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.models.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControllerClassDocProvider {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ControllerClassDocProvider(RequestMappingHandlerMapping requestMappingHandlerMapping){
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }
    public void inspectRequestMappings(SecurityMapperService mapperService, RASDSpecification spec) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Paths paths = mapperService.getSecurityDocumentation().getPaths();
        handlerMethods.entrySet().forEach(entry -> {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            if (shouldExcludeMapping(spec, handlerMethod)){
                return;
            }
            PathItem pathItem = setPathItem(mappingInfo, getOperations(entry, mapperService.getSecurityDocumentation().getMetaData(), mapperService));
            mappingInfo.getPatternValues().forEach(pattern -> paths.addPathItem(pattern, pathItem));
        });
    }

    private boolean shouldExcludeMapping(RASDSpecification spec, HandlerMethod handlerMethod){
        String packageName = handlerMethod.getBeanType().getPackage().getName();
        return spec.getExcludedPackages().stream()
                .anyMatch(packageName::startsWith);
    }

    private Operation getOperations (Map.Entry<RequestMappingInfo, HandlerMethod> entry, MetaData metaData, SecurityMapperService mapperService){
        Operation operation = new Operation();
        Class<?> handlerClass = entry.getValue().getBeanType();
        extractHasRoleFromPreAuthorize(handlerClass, entry.getValue().getMethod(), operation, mapperService);
        extractHasRoleFromPreAuthorize(handlerClass, entry.getValue().getMethod(), operation, mapperService);
        return  operation;
    }

    private PathItem setPathItem (RequestMappingInfo mappingInfo, Operation operation){
        PathItem pathItem = new PathItem();
        Set<RequestMethod> httpMethods = mappingInfo.getMethodsCondition().getMethods();

        httpMethods.forEach(httpMethod -> {
            switch (httpMethod) {
                case GET : pathItem.setGet(operation);
                        break;
                case POST : pathItem.setPost(operation);
                    break;
                case PUT : pathItem.setPut(operation);
                    break;
                case DELETE : pathItem.setDelete(operation);
                    break;
                case PATCH : pathItem.setPatch(operation);
                    break;
                case HEAD : pathItem.setHead(operation);
                    break;
                case OPTIONS : pathItem.setOptions(operation);
                    break;
                case TRACE : pathItem.setTrace(operation);
                    break;
                default : pathItem.setGet(operation);
            }
        });
        return pathItem;
    }

    private Set<String> extractHasRoleFromPreAuthorize(Class<?> handlerClass, Method handlerMethod, Operation operation, SecurityMapperService mapperService) {
        Set<String> roles = new HashSet<>();

        if (handlerMethod.isAnnotationPresent(PreAuthorize.class)) {
            PreAuthorize preAuthorize = handlerMethod.getAnnotation(PreAuthorize.class);
            extractRoleFromExpression(preAuthorize.value(), roles, operation, mapperService);
        }

        if (handlerClass.isAnnotationPresent(PreAuthorize.class)) {
            PreAuthorize preAuthorize = handlerClass.getAnnotation(PreAuthorize.class);
            extractRoleFromExpression(preAuthorize.value(), roles, operation, mapperService);
        }

        return roles;
    }

    private Set<String> extractRoleFromExpression(String expression, Set<String> roles, Operation operation, SecurityMapperService mapperService) {
        Pattern hasRolePattern = Pattern.compile("hasRole\\(['\"](.*?)['\"]\\)");
        Pattern hasAnyRolePattern = Pattern.compile("hasAnyRole\\((['\"].*?['\"])\\)");

        Matcher hasRoleMatcher = hasRolePattern.matcher(expression);
        while (hasRoleMatcher.find()) {
            mapperService.addStaticSingleRole(hasRoleMatcher.group(1), operation);
//            operation.getAuthorisationMod().getStaticMod().addRole(hasRoleMatcher.group(1));
        }

        Matcher hasAnyRoleMatcher = hasAnyRolePattern.matcher(expression);
        while (hasAnyRoleMatcher.find()) {
            String roleGroup = hasAnyRoleMatcher.group(1); // Extracted roles as a single string
            String[] roleArray = roleGroup.split(",\\s*['\"]");
            SecureModel secureModel = new SecureModel();

//            for (String role : roleArray) {
//                secureModel.getRoles().add(role.replaceAll("['\"]", ""));
//            }
//            operation.getAuthorisationMod().getRelativeMod().add(secureModel);
            List<String> roleList = Arrays.stream(roleArray).map(x -> x.replaceAll("['\"]", "")).collect(Collectors.toList());
            mapperService.addMultipleRoles(roleList, operation.getAuthorisationMod().getRelativeMod());

        }

        return roles;
    }
}
