package com.journey.central.journey.integrations;

import lombok.Data;

import java.util.Objects;

@Data
public class IntegrationTrigger {
    private String domain;
    private String intent;

    public IntegrationTrigger(String domain, String intent) {
        this.domain = domain;
        this.intent = intent;
    }

    @Override
    public String toString() {
        return "IntegrationTrigger [domain=" + domain + ", intent=" + intent + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegrationTrigger that = (IntegrationTrigger) o;
        return Objects.equals(domain, that.domain) && Objects.equals(intent, that.intent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, intent);
    }
}
