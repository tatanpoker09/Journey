package com.journey.central.journey.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class IntegrationManager {
    public static final String SCRIPTS_FOLDER = "scripts";
    private static final String NLP_FOLDER = "../journey_core_nlp";
    private static IntegrationManager instance;
    private List<Integration> integrations;
    private static final String INTEGRATION_FOLDER = "../integrations";
    private static final String INTEGRATION_CONFIG_NAME = "config.json";

    Logger logger = LoggerFactory.getLogger(IntegrationManager.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PythonConnector pythonConnector;


    @PostConstruct
    public void loadIntegrations(){
        integrations = new ArrayList<>();
        File integrationFolder = new File(INTEGRATION_FOLDER);
        if (!integrationFolder.exists()){
            logger.info("Integration folder does not exist");
            integrationFolder.mkdir();
        }
        for(File file : integrationFolder.listFiles()){
            if (!file.isDirectory()) {
                logger.warn("Found a file in the integration folder, ignoring");
                continue;
            }
            logger.info("Loading integration from folder: " + file.getName());
            loadIntegration(file);
        }
    }

    private void loadIntegration(File integrationFolder){
        File configFile = new File(integrationFolder.getAbsolutePath() + File.separator + INTEGRATION_CONFIG_NAME);
        if (!configFile.exists()){
            logger.warn("Integration config file does not exist. Skiping integration");
            return;
        }
        try {
            logger.info("Loading integration config file: " + configFile.getAbsolutePath());
            IntegrationConfig integrationConfig = objectMapper.readValue(configFile, IntegrationConfig.class);
            File parentDirectory = configFile.getParentFile();
            //Copy python files.
            copyScripts(parentDirectory);
            //Copy nlp folders.
            copyNLPFolders(parentDirectory);

            Integration integration = new Integration(parentDirectory, integrationConfig);
            integration.load();
            integrations.add(integration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyNLPFolders(File parentDirectory) throws IOException {
        File nlpFolder = new File(parentDirectory.getAbsolutePath() + File.separator + "nlp");
        if (!nlpFolder.exists()){
            logger.warn("NLP folder does not exist. Skipping");
            return;
        }
        File originalDomainsFolder = new File(nlpFolder + File.separator + "domains");
        File originalEntitiesFolder = new File(nlpFolder + File.separator + "entities");
        File targetDomainsFolder = new File(NLP_FOLDER+ File.separator + "domains");
        File targetEntitiesFolder = new File(NLP_FOLDER + File.separator + "entities");
        if (originalDomainsFolder.exists()) {
            logger.info("Copying NLP domains folder");
            for(File file : Objects.requireNonNull(originalDomainsFolder.listFiles())){
                File newFile = new File(targetDomainsFolder.getAbsolutePath() + File.separator + file.getName());
                FileUtils.copyDirectory(file, newFile, true);
            }
        } else {
            logger.warn("NLP domains folder does not exist. Skipping");
        }
        if (originalEntitiesFolder.exists()) {
            logger.info("Copying NLP entities folder");
            for(File file : Objects.requireNonNull(originalEntitiesFolder.listFiles())){
                File newFile = new File(targetEntitiesFolder.getAbsolutePath() + File.separator + file.getName());
                FileUtils.copyDirectory(file, newFile, true);
            }
        } else {
            logger.warn("NLP entities folder does not exist. Skipping");
        }
    }

    private void copyScripts(File integrationDirectory) throws IOException {
        for(File file : Objects.requireNonNull(integrationDirectory.listFiles())){
            if (file.getName().endsWith(".py")){
                File newFile = new File(SCRIPTS_FOLDER + File.separator + file.getName());

                logger.info("Copying python file: " + file.getAbsolutePath() + " to " + newFile.getAbsolutePath());
                if(newFile.exists()){
                    logger.warn("Python file already exists. Will replace old file.");
                }
                Files.copy(file.toPath(), newFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public void onTrigger(IntegrationTrigger trigger, Map<String, String> entities){
        for(Integration integration : integrations){
            integration.onTrigger(trigger, entities, pythonConnector);
        }
    }

}
