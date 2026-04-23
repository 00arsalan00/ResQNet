package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum IncidentResourceStatus {
    ALLOCATED,
    IN_USE,
    COMPLETED,
    CANCELLED;

    @JsonCreator
    public static IncidentResourceStatus from(String value) {
        return IncidentResourceStatus.valueOf(value.toUpperCase());
    }
}