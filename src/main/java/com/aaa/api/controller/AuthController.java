package com.aaa.api.controller;

import com.aaa.api.config.security.jwt.JwtTokenProvider;
import com.aaa.api.config.security.jwt.JwtTokenReIssueProvider;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.JwtToken;
import com.aaa.api.dto.response.SessionResponse;
import com.aaa.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenReIssueProvider reIssueProvider;

    @PostMapping("login")
    public ResponseEntity<?> signIn(@RequestBody @Validated LoginRequest loginRequest, HttpServletRequest request) {

        JwtToken jwtToken = authService.login(loginRequest);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken.getAccessToken());
        httpHeaders.add("Refresh-Token", jwtToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwtToken);
    };

    @PatchMapping("reissue")
    public ResponseEntity<?> reIssueRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("Refresh-Token");
        String username = reIssueProvider.validateRefreshToken(refreshToken);
        String reIssueAccessToken = reIssueProvider.reIssueAccessToken(username);

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + reIssueAccessToken);
        httpHeaders.add("Refresh-Token", refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body("refresh");
    }

}
