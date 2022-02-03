package com.journey.central.journey.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

public class PythonExecutor extends NotifyingThread{
    private final IntegrationMethod integrationMethod;
    private List<String> parameters;


    public PythonExecutor(IntegrationMethod integrationMethod, List<String> parameters){
        this.integrationMethod = integrationMethod;
        this.parameters = parameters;
    }

    @Override
    public void doRun() {
        //Send post request to python executor service.
        //For now we simulate it.
        Logger logger = LoggerFactory.getLogger(Integration.class);
        logger.info("PythonExecutor: Sending post request to python executor service.");
        logger.info("PythonExecutor: {} {}", integrationMethod.getMethodName(), parameters);
        HttpClient client = HttpClient.newHttpClient();
        try {
            var objectMapper = new ObjectMapper();
            HashMap<String, Object> values = new HashMap<>() {{
                put("script_name", integrationMethod.getFileName());
                put("script_arguments", "[]");
            }};

            String requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(values);

            HttpRequest request = HttpRequest.newBuilder(URI.create("http://journeyintegrations:9090/script"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            values = new HashMap<>() {{
                put("method_name", integrationMethod.getMethodName());
                put("method_arguments", parameters);
            }};
            requestBody = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(values);

            request = HttpRequest.newBuilder()
                    .uri(URI.create("http://journeyintegrations:9090/method"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}