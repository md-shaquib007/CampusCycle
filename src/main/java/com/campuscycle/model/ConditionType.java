package com.campuscycle.model;

public enum ConditionType {
    NEW, LIKE_NEW, GOOD, FAIR, POOR;

    public static ConditionType fromString(String value) {
        if (value == null) return GOOD;
        return ConditionType.valueOf(value.toUpperCase());
    }
}
