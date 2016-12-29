package service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceConfiguration extends io.dropwizard.Configuration {

    private int maxLength;

    @JsonProperty
    public int getMaxLength() {
        return maxLength;
    }

    @JsonProperty
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}