package com.aaa.api.domain.enumType;

import lombok.Getter;

@Getter
public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    String role;

    Role(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
