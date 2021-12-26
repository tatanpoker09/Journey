package com.journey.central.journey.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Dialogue {
    @Id
    private long id;
    private String prompt;
    private String response;
    private String intent;
    private String domain;


}
