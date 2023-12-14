package com.aaa.api.controller;

import com.aaa.api.config.YmlProperties;
import com.aaa.api.config.data.UserSession;
import com.aaa.api.domain.Users;
import com.aaa.api.dto.request.LoginRequest;
import com.aaa.api.dto.response.SessionResponse;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
    private final YmlProperties ymlProperties;


    //JWT Test용
    @GetMapping("/foo")
    public Long foo(UserSession userSession){
        return userSession.getId();
    }


    @PostMapping("login")
    public SessionResponse signIn(@RequestBody @Validated LoginRequest loginRequest){

        Long userId = authService.login(loginRequest);

        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(ymlProperties.getJwtKey()));

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(secretKey)
                .compact();

        return new SessionResponse(jws);
    }
}
