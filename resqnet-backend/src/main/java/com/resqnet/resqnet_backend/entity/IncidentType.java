package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum IncidentType {
    FIRE,
    FLOOD,
    EARTHQUAKE;

    @JsonCreator
    public static IncidentType from(String value) {
        return IncidentType.valueOf(value.toUpperCase());
    }
}
