package com.aaa.api.controller;

import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.JwtToken;
import com.aaa.api.dto.response.SessionResponse;
import com.aaa.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class AuthController {

    private final AuthService authService;


    @PostMapping("login")
    public ResponseEntity<?> signIn(@RequestBody @Validated LoginRequest loginRequest, HttpServletRequest request) {

        JwtToken jwtToken = authService.login(loginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken.getAccessToken());
        headers.add("Refresh-Token", jwtToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(jwtToken);
    };

}
