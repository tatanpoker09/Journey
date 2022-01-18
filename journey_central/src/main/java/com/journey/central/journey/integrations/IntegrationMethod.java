package com.journey.central.journey.integrations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@Configurable
public class IntegrationMethod {
    @NotNull
    private List<IntegrationTrigger> triggers;

    @JsonProperty("destroy_triggers")
    private List<IntegrationTrigger> destroyTriggers;

    private List<String> entities;
    private List<String> parameters;

    @NotNull
    @JsonProperty("file_name")
    private String fileName;

    public void execute(Map<String, String> entities, PythonConnector pythonConnector) {
        if (pythonConnector != null) {
            pythonConnector.execute(this, entities);
        }
    }

    public void destroy(Map<String, String> entities, PythonConnector pythonConnector) {
        if (pythonConnector != null) {
            pythonConnector.destroy(this, entities);
        }
    }
}
