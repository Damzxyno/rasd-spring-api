package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.securitycask.HttpSecurity;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.damzxyno.rasdspringapi.compiler.DynamicCompilerRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.MethodMetadata;
import org.springframework.security.web.SecurityFilterChain;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ConfigurationClassDocProvider {
    private ApplicationContext context;
    private SecurityMapperService securityMapperService;
    private DynamicCompilerRunner runner;
    public ConfigurationClassDocProvider(SecurityMapperService securityMapperService) {
        this.securityMapperService = securityMapperService;
        this.runner = new DynamicCompilerRunner();
    }

    public void inspectRequestMappings() {
        Set<String> securityConfigClassNames = findSecurityFilterChainBeanClassName();
        securityConfigClassNames.forEach(className -> {
            String packageName = "com.damzxyno.rasdspringapi.securitycask";
            String classFileContent = decompileClassContent(className);
            classFileContent = removeSpecifiedAnnotations(classFileContent);
            classFileContent = replacePackageDeclaration(classFileContent, packageName);
            classFileContent = removeSpringSecurityImports(classFileContent);
            HttpSecurity httpSecurity = new HttpSecurity(securityMapperService);
            runner.run(packageName, extractClassName(className), classFileContent, httpSecurity);
        });
    }

    private String extractClassName(String packageName){
        String [] classPath = packageName.split("\\.");
        return classPath[classPath.length - 1];
    }

    private String decompileClassContent(String className){
        final DecompilerSettings settings = DecompilerSettings.javaDefaults();
        StringWriter stringWriter = new StringWriter();

        Decompiler.decompile(
                className,
                new PlainTextOutput(stringWriter),
                settings
        );
        return stringWriter.toString();
    }

    private String removeSpecifiedAnnotations(String classFileContent) {
        // Regular expression to match specific annotations
        String regex = "(?m)@Configuration\\s*|@EnableWebSecurity\\s*|@EnableMethodSecurity\\s*|@Bean\\s*";

        // Remove the specified annotations and any trailing whitespace or newlines
        return classFileContent.replaceAll(regex, "")
                .replaceAll("(?m)^\\s*\\n", ""); // Remove any empty lines left over
    }


    private String replacePackageDeclaration(String classFileContent, String newPackageName) {
        return classFileContent.replaceAll("(?m)^package\\s+[^;]+;", "package " + newPackageName + ";");
    }

    private String removeSpringSecurityImports(String classFileContent) {
        // Regular expression to match import statements that start with org.springframework.security
        return classFileContent.replaceAll("(?m)^import\\s+org\\.springframework\\.security[^;]+;\\s*\\n?", "");
    }


    private Set<String> findSecurityFilterChainBeanClassName() {
        AbstractApplicationContext abstractContext = (AbstractApplicationContext)context;
        String[] beanNames = context.getBeanNamesForType(SecurityFilterChain.class);
        Set<String> securityConfigClasses = new HashSet<>();
        Arrays.stream(beanNames).forEach(beanName ->{
            AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) abstractContext.getBeanFactory().getBeanDefinition(beanName);
            MethodMetadata methodMetadata = beanDefinition.getFactoryMethodMetadata();
            if (methodMetadata != null && methodMetadata.isAnnotated(Bean.class.getName())) {
                securityConfigClasses.add(methodMetadata.getDeclaringClassName());
            }
        });
        return securityConfigClasses;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
