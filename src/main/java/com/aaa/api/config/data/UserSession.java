package com.aaa.api.config.data;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSession {

    private Long id;
    private String name;

    @Builder
    public UserSession(Long id) {
        this.id = id;
    }
}
