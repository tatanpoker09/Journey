package com.journey.central.journey.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Integration {
    private File filePath;
    private IntegrationConfig integrationConfig;
    private Map<IntegrationTrigger, IntegrationMethod> methods; //map from domain:intent to method.

    Logger logger = LoggerFactory.getLogger(Integration.class);


    public Integration(File filePath, IntegrationConfig integrationConfig) {
        this.filePath = filePath;
        this.integrationConfig = integrationConfig;
        this.methods = new HashMap<>();
        loadMethods(integrationConfig.getMethods());
    }

    private void loadMethods(List<IntegrationMethod> integrationMethods) {
        logger.info("Loading {} methods: ", integrationMethods.size());
        if( methods == null) {
            methods = new HashMap<>();
        }
        for (IntegrationMethod integrationMethod : integrationMethods) {
            logger.info("Found method: "+integrationMethod.getFileName()+" "+integrationMethod.getParameters());
            for (IntegrationTrigger integrationTrigger : integrationMethod.getTriggers()) {
                methods.put(integrationTrigger, integrationMethod);
            }
        }
    }


    private boolean isService() {
        return integrationConfig.isService();
    }

    public void load() {
        if(isService()) {
            //TODO We initialize straight away.
        }
        String name = integrationConfig.getName();
        String description = integrationConfig.getDescription();
        String version = integrationConfig.getVersion();
        List<String> authors = integrationConfig.getAuthors();
        logger.info("Loaded integration: " + name+ " " + version+ " " + description+ " " + authors);
    }

    public void onTrigger(IntegrationTrigger trigger, Map<String, String> entities, PythonConnector pythonConnector){
        IntegrationMethod integrationMethod = methods.get(trigger);
        if(integrationMethod != null) {
            integrationMethod.execute(entities, pythonConnector);
        }
    }
}
