package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ResourceType {
    FOOD,
    WATER,
    MEDICINE,
    SHELTER;

    @JsonCreator
    public static ResourceType from(String value) {
        return ResourceType.valueOf(value.toUpperCase());
    }
}
