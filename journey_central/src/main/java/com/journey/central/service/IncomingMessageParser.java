package com.journey.central.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class IncomingMessageParser {
    @Autowired
    private KafkaProducer producer;

    @Autowired
    private IntegrationManager integrationManager;
    public void parseIncomingMessage(IncomingMessage incomingMessage){
        String intent = incomingMessage.getIntent();
        String integration = integrationManager.getIntegration(intent);
        String topic = getTopic(integration, intent);
        OutgoingMessage outgoingMessage = new OutgoingMessage(intent, incomingMessage.getEntities());
        try {
            producer.sendMessage(topic, outgoingMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTopic(String integration, String intent) {
        return integration + "." + intent;
    }
}