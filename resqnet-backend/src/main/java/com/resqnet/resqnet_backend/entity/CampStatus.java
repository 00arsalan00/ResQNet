package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CampStatus {
    ACTIVE,
    INACTIVE,
    FULL;

    @JsonCreator
    public static CampStatus from(String value) {
        return CampStatus.valueOf(value.toUpperCase());
    }
}
