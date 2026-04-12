package com.resqnet.resqnet_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SkillType {
    MEDICAL,
    RESCUE,
    LOGISTIC,
    FIRE_FIGHTING;

    @JsonCreator
    public static SkillType from(String value) {
        return SkillType.valueOf(value.toUpperCase());
    }
}
