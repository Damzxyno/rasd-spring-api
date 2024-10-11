package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.models.*;
import com.damzxyno.rasdspringapi.utils.RASDSpecification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerClassDocProvider {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final SecurityMapperService securityMapperService;
    private final Map<String, AuthorisationMod> classSecRequirementCache;

    public ControllerClassDocProvider(RequestMappingHandlerMapping requestMappingHandlerMapping, SecurityMapperService securityMapperService){
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.securityMapperService = securityMapperService;
        classSecRequirementCache = new HashMap<>();
    }

    public void inspectRequestMappings(RASDSpecification spec) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        handlerMethods.forEach((mappingInfo, handlerMethod) -> {
            if (shouldExcludeMapping(spec, handlerMethod)) {
                return;
            }
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            AuthorisationMod authorisationMod = extractAuthorisationReqFromPreAuthorizeAnnotation(handlerMethod);
            mappingInfo.getPatternValues().forEach(pattern -> {
                mappingInfo.getMethodsCondition().getMethods().forEach(method -> {
                    securityMapperService.addAuthorisationMod(pattern, method, authorisationMod);
                    securityMapperService.addTagToPatternOperation(pattern, method, controllerName);
                });
            });
        });
    }


    private AuthorisationMod extractAuthorisationReqFromPreAuthorizeAnnotation(HandlerMethod handlerMethodInf) {

        Class<?> handlerClass = handlerMethodInf.getBeanType();
        Method handlerMethod = handlerMethodInf.getMethod();
        AuthorisationMod authorisationMod = new AuthorisationMod();

        // Extract Requirements from handler methods.
        if (handlerMethod.isAnnotationPresent(PreAuthorize.class)) {
            PreAuthorize preAuthorize = handlerMethod.getAnnotation(PreAuthorize.class);
            AuthorisationMod methodAuthorisationMod = SecuritySpELHandler.getInstance()
                    .parseExpressionAndBuildAuthorisationMod(preAuthorize.value());
            securityMapperService.addMissingAuthorisationModFromBIntoA(authorisationMod, methodAuthorisationMod);
        }

        // Extract Requirement from handler class.
        if (handlerClass.isAnnotationPresent(PreAuthorize.class)) {
            if (classSecRequirementCache.containsKey(handlerClass.getName())){
                AuthorisationMod classAuthorisationMod = classSecRequirementCache.get(handlerClass.getName());
                securityMapperService.addMissingAuthorisationModFromBIntoA(authorisationMod, classAuthorisationMod);
            } else {
                PreAuthorize preAuthorize = handlerClass.getAnnotation(PreAuthorize.class);
                AuthorisationMod classAuthorisationMod = SecuritySpELHandler.getInstance()
                        .parseExpressionAndBuildAuthorisationMod(preAuthorize.value());
                securityMapperService.addMissingAuthorisationModFromBIntoA(authorisationMod, classAuthorisationMod);
                classSecRequirementCache.put(handlerClass.getName(), classAuthorisationMod);
            }
        }
        return authorisationMod;
    }

    private boolean shouldExcludeMapping(RASDSpecification spec, HandlerMethod handlerMethod){
        String packageName = handlerMethod.getBeanType().getPackage().getName();
        return spec.getExcludedPackages().stream()
                .anyMatch(packageName::startsWith);
    }
}
