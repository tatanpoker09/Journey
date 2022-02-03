package com.journey.central.journey.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PythonConnector implements ThreadCompleteListener{
    private static final String SCRIPTS_FOLDER = IntegrationManager.SCRIPTS_FOLDER;
    private String pythonCommand = "python";
    private Logger logger = LoggerFactory.getLogger(PythonConnector.class);


    @PostConstruct
    public void init() {

    }

    private void jythonInit() {

    }

    public void checkPythonVersion(){
    }

    public void execute(IntegrationMethod integrationMethod, Map<String, String> entities) {
        //TODO modify this to use the Flask service.
        //First we check if there is already a process running this method, and we destroy it in case there is.

        List<String> parameters = new ArrayList<>();
        for (String value : integrationMethod.getParameters()){
            if(value.contains("{") && value.contains("}")){
                String key = value.substring(value.indexOf("{") + 1, value.indexOf("}"));
                if (integrationMethod.getEntities().contains(key)) {
                    if (entities.containsKey(key)) {
                        parameters.add('"'+entities.get(key)+'"');
                    }
                }
            } else {
                parameters.add(value);
            }
        }
        PythonExecutor pythonExecutor = new PythonExecutor(integrationMethod, parameters);
        pythonExecutor.addListener(this);
        pythonExecutor.start();
    }

    @PreDestroy
    public void onShutdown(){
        logger.info("Shutting down integration manager");
        //TODO kill the threads in the executor service.
    }

    @Override
    public void onThreadComplete(Thread thread) {
        //This might still be useful later on.
    }
}
