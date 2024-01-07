package com.aaa.api.service.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
