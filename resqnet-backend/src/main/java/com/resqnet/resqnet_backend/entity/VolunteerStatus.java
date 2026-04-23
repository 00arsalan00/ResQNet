package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VolunteerStatus {
    AVAILABLE,
    ASSIGNED,
    INACTIVE;

    @JsonCreator
    public static VolunteerStatus from(String value) {
        return VolunteerStatus.valueOf(value.toUpperCase());
    }
}
