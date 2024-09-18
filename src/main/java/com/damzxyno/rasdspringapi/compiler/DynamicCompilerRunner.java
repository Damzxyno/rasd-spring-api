package com.damzxyno.rasdspringapi.compiler;

import com.damzxyno.rasdspringapi.securitycask.HttpSecurity;
import com.damzxyno.rasdspringapi.securitycask.SecurityFilterChain;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Constructor;
import java.util.Collections;

public class DynamicCompilerRunner {

    public void run(String packageName, String className, String sourceCode, HttpSecurity httpSecurity) {
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            InMemoryFileManager fileManager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null));
            JavaSourceFromString javaSource = new JavaSourceFromString(packageName + "." + className, sourceCode);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, Collections.singletonList(javaSource));
            boolean success = task.call();

            if (success) {
                Class<?> clazz = fileManager.getClassLoader(null).loadClass(packageName + "." + className);
                Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                Object[] initargs = new Object[constructors[0].getParameterCount()];
                Object newInstance = constructors[0].newInstance(initargs);

                SecurityFilterChain filterChain = (SecurityFilterChain) clazz
                        .getMethod("securityFilterChain", HttpSecurity.class)
                        .invoke(newInstance, httpSecurity);
            } else {
                System.out.println("Compilation failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
