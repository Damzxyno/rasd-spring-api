package com.damzxyno.rasdspringapi.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Paths extends LinkedHashMap<String, PathItem> {
    private Map<String, Object> extensions = new HashMap<>();

    public Paths() {
    }

    public Paths addPathItem(String name, PathItem item) {
        this.put(name, item);
        return this;
    }

    public Map<String, Object> getExtensions() {
        return this.extensions;
    }

    public void addExtension(String name, Object value) {
        if (name != null && !name.isEmpty() && name.startsWith("x-")) {
            if (this.extensions == null) {
                this.extensions = new LinkedHashMap();
            }

            this.extensions.put(name, value);
        }
    }

    public void addExtension31(String name, Object value) {
        if (name == null || !name.startsWith("x-oas-") && !name.startsWith("x-oai-")) {
            this.addExtension(name, value);
        }
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public Paths extensions(Map<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }
}
