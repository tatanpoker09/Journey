package com.journey.central.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, OutgoingMessage message) throws JsonProcessingException {
        logger.info(String.format("#### -> Producing message -> %s", message));
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedMessage = objectMapper.writeValueAsString(message);
        this.kafkaTemplate.send(topic, serializedMessage);
    }
}