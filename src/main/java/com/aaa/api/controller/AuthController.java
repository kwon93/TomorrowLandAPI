package com.aaa.api.controller;

import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.JwtToken;
import com.aaa.api.dto.response.SessionResponse;
import com.aaa.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public JwtToken signIn(@RequestBody @Validated LoginRequest loginRequest){

        return authService.login(loginRequest);

    }
}
