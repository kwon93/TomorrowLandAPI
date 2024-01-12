package com.aaa.api.domain.enumType;

public enum UserLevel {

    Beginner("Beginner"),
    Intermediate("Intermediate"),
    Advanced("Advanced");

    private final String level;
    UserLevel(String level) {
        this.level = level;
    }
}
