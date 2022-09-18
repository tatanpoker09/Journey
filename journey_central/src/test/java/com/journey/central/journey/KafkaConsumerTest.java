package com.journey.central.journey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.journey.central.service.Entity;
import com.journey.central.service.IncomingMessage;
import com.journey.central.service.KafkaConsumer;
import com.journey.central.service.OutgoingMessage;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KafkaConsumerTest {
    @Test
    public void deserialize(){
        String incoming = "{\"msg\": \"message telegram sweta\", \"intent\": \"telegram_message\", \"entities\": [{\"name\": \"Sweta\", \"type\": \"person\", \"chat_id\": \"5223390622\"}]}";

        ObjectMapper mapper = new ObjectMapper();
        IncomingMessage incomingMessage = null;
        try {
            incomingMessage = mapper.readValue(incoming, IncomingMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(incomingMessage);
    }

    @Test
    public void serialize(){
        List<Entity> entities = new ArrayList<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("chat_id", "5223390622");
        entities.add(new Entity("Sweta", "person", attributes));
        OutgoingMessage outgoingMessage = new OutgoingMessage("telegram_message", entities);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String string = objectMapper.writeValueAsString(outgoingMessage);
            System.out.println(string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void receiveAndSend(){
        String incoming = "{\"msg\": \"message telegram sweta\", \"intent\": \"telegram_message\", \"entities\": [{\"name\": \"Sweta\", \"type\": \"person\", \"chat_id\": \"5223390622\"}]}";

        ObjectMapper mapper = new ObjectMapper();
        IncomingMessage incomingMessage = null;
        try {
            incomingMessage = mapper.readValue(incoming, IncomingMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OutgoingMessage outgoingMessage = new OutgoingMessage(incomingMessage.getIntent(), incomingMessage.getEntities());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String string = objectMapper.writeValueAsString(outgoingMessage);
            System.out.println(string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
