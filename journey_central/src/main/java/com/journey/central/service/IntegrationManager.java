package com.journey.central.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IntegrationManager {
    private Map<String, String> intentIntegrationMap;

    public String getIntegration(String intent){
        if (intent.equals("telegram_message")){
            return "telegram_sender";
        }
        return "undefined";
    }
}
