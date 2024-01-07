package com.aaa.api.service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginServiceRequest {

    private final String email;
    private final String password;

    @Builder
    public LoginServiceRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
