package com.journey.central.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Entity
@Data
public class Dialogue {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String prompt;
    @NotNull
    private String response;
    @NotNull
    private String intent;
    private String domain;

    @Transient
    private HashMap<String, String> entities;

    public Dialogue(long id, String prompt, String response, String intent, String domain, String entities) {
        ObjectMapper mapper = new ObjectMapper();
        this.id = id;
        this.prompt = prompt;
        this.response = response;
        this.intent = intent;
        this.domain = domain;
        try {
            this.entities = mapper.readValue(entities, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Dialogue() {

    }
}
