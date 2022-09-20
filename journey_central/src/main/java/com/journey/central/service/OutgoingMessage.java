package com.journey.central.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.journey.central.service.Entity;

import java.io.IOException;
import java.util.List;

public class OutgoingMessage {
    @JsonProperty("intent")
    private String intent;
    @JsonProperty("entities")
    @JsonSerialize(using = EntitySerializer.class)
    private List<Entity> entities;

    public OutgoingMessage(String intent, List<Entity> entities) {
        this.intent = intent;
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "intent='" + intent + '\'' +
                ", entities=" + entities +
                '}';
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}

class EntitySerializer extends JsonSerializer<List<Entity>> {

    @Override
    public void serialize(List<Entity> value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartArray();
        for (Entity entity : value) {
            jgen.writeStartObject();
            jgen.writeObjectField("entity", entity);
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }

}