package com.journey.central.journey.integrations;

import java.util.List;

public class IntegrationConfig {
    private String name;
    private String description;
    private String version;
    private boolean service;
    private List<String> authors;
    private List<IntegrationMethod> methods;

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthor(String author) {
        authors = List.of(author);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public void setMethods(List<IntegrationMethod> methods) {
        this.methods = methods;
    }

    public List<IntegrationMethod> getMethods() {
        return methods;
    }
}
