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

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtToken> signIn(@RequestBody @Validated LoginRequest loginRequest) {

        JwtToken jwtToken = authService.login(loginRequest.toServiceDto());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken.getAccessToken());
        httpHeaders.add("Refresh-Token", jwtToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwtToken);
    };

    @PatchMapping("reissue")
    public ResponseEntity<String> reIssueRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader("Refresh-Token");

        String reIssueAccessToken = authService.reissueAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + reIssueAccessToken);
        httpHeaders.add("Refresh-Token", refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body("refresh");
    }

}
