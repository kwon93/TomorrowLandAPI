package com.aaa.api.dto.response;

import jakarta.persistence.GeneratedValue;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
