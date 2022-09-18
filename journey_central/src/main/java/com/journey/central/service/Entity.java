package com.journey.central.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

@JsonDeserialize(using = EntityDeserializer.class)
public class Entity {
    private String name;
    private String type;
    private Map<String, String> attributes;

    public Entity (String name, String type, Map<String, String> attributes){
        this.name = name;
        this.type = type;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString(){
        return String.format("name: %s, type: %s, attributes_len: %d", name, type, attributes.size());
    }
}