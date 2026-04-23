package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum IncidentStatus {
    REPORTED,
    VERIFIED,
    IN_PROGRESS,
    RESOLVED;

    @JsonCreator
    public static IncidentStatus from(String value) {
        return IncidentStatus.valueOf(value.toUpperCase());
    }
}
