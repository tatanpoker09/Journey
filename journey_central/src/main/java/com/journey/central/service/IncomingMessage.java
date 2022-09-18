package com.journey.central.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomingMessage {
    @JsonProperty("msg")
    private String message;
    @JsonProperty("intent")
    private String intent;
    @JsonProperty("entities")
    private List<Entity> entities;

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "message='" + message + '\'' +
                ", intent='" + intent + '\'' +
                ", entities=" + entities +
                '}';
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public String getIntent() {
        return intent;
    }
}