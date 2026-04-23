package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TeamStatus {
    AVAILABLE,
    BUSY,
    OFFLINE;

    @JsonCreator
    public static TeamStatus from(String value) {
        return TeamStatus.valueOf(value.toUpperCase());
    }
}
