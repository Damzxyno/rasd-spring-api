package com.damzxyno.rasdspringapi.compiler;

import javax.tools.*;
import java.util.Hashtable;
import java.util.Map;

public class InMemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, JavaClassAsBytes> compiledClasses;
    private final ClassLoader loader;

    public InMemoryFileManager(StandardJavaFileManager standardManager) {
        super(standardManager);
        this.compiledClasses = new Hashtable<>();
        this.loader = new InMemoryClassLoader(this.getClass().getClassLoader(), this);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location,
                                               String className, JavaFileObject.Kind kind, FileObject sibling) {
        JavaClassAsBytes classAsBytes = new JavaClassAsBytes(className, kind);
        compiledClasses.put(className, classAsBytes);

        return classAsBytes;
    }

    public Map<String, JavaClassAsBytes> getBytesMap() {
        return compiledClasses;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return loader;
    }
}
