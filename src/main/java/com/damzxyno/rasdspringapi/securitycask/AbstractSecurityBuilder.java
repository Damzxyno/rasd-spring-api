package com.damzxyno.rasdspringapi.securitycask;

public class AbstractSecurityBuilder <T> {
    protected T instance;

    public AbstractSecurityBuilder(T instance) {
        this.instance = instance;
    }

    // Example method that returns the instance
    public T build() {
        // Perform any final modifications or validations here
        return instance;
    }
}
