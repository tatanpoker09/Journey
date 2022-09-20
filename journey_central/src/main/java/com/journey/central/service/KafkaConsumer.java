package com.journey.central.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class KafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    @Autowired
    private final IncomingMessageParser incomingMessageParser = new IncomingMessageParser();

    @KafkaListener(topics = "journey_central.incoming_messages", groupId = "journey_central")
    public void consume(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message! -> %s", message));
        ObjectMapper mapper = new ObjectMapper();
        IncomingMessage incomingMessage = mapper.readValue(message, IncomingMessage.class);
        logger.info(incomingMessage.toString());
        incomingMessageParser.parseIncomingMessage(incomingMessage);
    }
}

class EntityDeserializer extends StdDeserializer<Entity> {
    public EntityDeserializer(){
        this(null);
    }
    protected EntityDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Entity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get("name").asText();
        String type = node.get("type").asText();
        Iterator<Map.Entry<String, JsonNode>> elements = node.fields();

        Map<String, String> entities = new HashMap<>();
        while(elements.hasNext()){
            Map.Entry<String, JsonNode> next = elements.next();
            String key = next.getKey();
            if (key.equals("type") || key.equals("name")){
                continue;
            }

            String value = next.getValue().asText();
            entities.put(key, value);
        }

        return new Entity(name, type, entities);
    }
}



