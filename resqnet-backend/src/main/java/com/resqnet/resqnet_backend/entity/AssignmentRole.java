package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AssignmentRole {
    MEDIC,
    SEARCH_RESCUE,
    LOGISTICS,
    CAMP_SUPPORT;

    @JsonCreator
    public static AssignmentRole from(String value) {
        return AssignmentRole.valueOf(value.toUpperCase());
    }
}
