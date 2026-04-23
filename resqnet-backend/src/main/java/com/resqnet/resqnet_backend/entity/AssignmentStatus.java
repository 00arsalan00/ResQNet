package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AssignmentStatus {
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED;

    @JsonCreator
    public static AssignmentStatus from(String value) {
        return AssignmentStatus.valueOf(value.toUpperCase());
    }
}
