package com.journey.central.journey.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PythonConnector implements ThreadCompleteListener{
    private static final String SCRIPTS_FOLDER = IntegrationManager.SCRIPTS_FOLDER;
    private String pythonCommand = "python";
    private Logger logger = LoggerFactory.getLogger(PythonConnector.class);
    private Map<IntegrationMethod, PythonExecutor> processThreads;


    @PostConstruct
    public void init() {
        processThreads = new HashMap<>();
        checkPythonVersion();
        jythonInit();
    }

    private void jythonInit() {

    }

    public void checkPythonVersion(){
        if(!pythonAvailable()){
            if(python3Available()) {
                pythonCommand = "python3";
            } else {
                //TODO install python
            }
        }
    }

    private boolean pythonAvailable() {
        try {
            Runtime.getRuntime().exec("python --version");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean python3Available() {
        try {
            Runtime.getRuntime().exec("python3 --version");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void execute(IntegrationMethod integrationMethod, Map<String, String> entities) {
        //First we check if there is already a process running this method, and we destroy it in case there is.
        PythonExecutor oldExecutor = processThreads.get(integrationMethod);
        if(oldExecutor != null){
            oldExecutor.getProcess().destroy();
        }

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
        List<String> command = new ArrayList<>();
        //command.add(pythonCommand);
        command.add(SCRIPTS_FOLDER+ File.separator+integrationMethod.getFileName());
        File file = new File(SCRIPTS_FOLDER+ File.separator+integrationMethod.getFileName());
        command.addAll(parameters);
        logger.info("Executing command: " + command);
        //PythonExecutor pythonExecutor = new PythonExecutor(command);
        PythonExecutor pythonExecutor = new PythonExecutor(file);
        pythonExecutor.addListener(this);
        processThreads.put(integrationMethod, pythonExecutor);
        pythonExecutor.start();
    }

    @PreDestroy
    public void onShutdown(){
        logger.info("Shutting down integration manager");
        for(PythonExecutor executor : processThreads.values()){
            executor.getProcess().destroyForcibly();
            executor.interrupt();
        }
    }

    @Override
    public void onThreadComplete(Thread thread) {
        for (Map.Entry<IntegrationMethod, PythonExecutor> pythonExecutorEntry : processThreads.entrySet()) {
            if (pythonExecutorEntry.getValue() == thread) {
                processThreads.remove(pythonExecutorEntry.getKey());
            }
        }
    }

    public void destroy(IntegrationMethod integrationMethod, Map<String, String> entities) {
        //We should be able to ignore the entities for now (later on we made need locations to stop things only at certain areas.
        PythonExecutor pythonExecutor = processThreads.get(integrationMethod);
        pythonExecutor.getProcess().destroy();
    }
}
