package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class AuthController {

    private static final String JWT_AUTH_HEADER = "Authorization";
    private static final String JWT_REFRESH = "Refresh-Token";
    private static final String  GRANT_TYPE = "Bearer ";
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtToken> signIn(@RequestBody @Validated final LoginRequest loginRequest) {

        final JwtToken jwtToken = authService.login(loginRequest.toServiceDto());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + jwtToken.getAccessToken());
        httpHeaders.add(JWT_REFRESH, jwtToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwtToken);
    };

    @PatchMapping("reissue")
    public ResponseEntity<String> reIssueRefreshToken(final HttpServletRequest request){
        final String refreshToken = request.getHeader(JWT_REFRESH);
        final String reIssueAccessToken = authService.reissueAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + reIssueAccessToken);
        httpHeaders.add(JWT_REFRESH, refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body("refresh");
    }

}
