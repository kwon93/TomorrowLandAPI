package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.service.dto.response.JwtToken;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.dto.request.LoginServiceRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class AuthController {

    private static final String JWT_AUTH_HEADER = "Authorization";
    private static final String JWT_REFRESH = "RefreshToken";
    private static final String  GRANT_TYPE = "Bearer ";
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<JwtToken> signIn(@RequestBody @Validated final LoginRequest loginRequest) {
        final JwtToken jwtToken = authService.login(loginRequest.toServiceDto());

        final HttpHeaders httpHeaders = new HttpHeaders();
        ResponseCookie refreshCookie = getRefreshCookie(jwtToken.getRefreshToken());
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + jwtToken.getAccessToken());
        httpHeaders.add("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwtToken);
    }

    @PatchMapping("reissue")
    public ResponseEntity<String> reIssueRefreshToken(final HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JWT_REFRESH))
                .findFirst()
                .get()
                .getValue();
        final String reIssueAccessToken = authService.reissueAccessToken(refreshToken);

        ResponseCookie refreshCookie = getRefreshCookie(refreshToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + reIssueAccessToken);
        httpHeaders.add("Set-Cookie",refreshCookie.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body("refresh");
    }


    private static ResponseCookie getRefreshCookie(String refreshToken) {
        return ResponseCookie.from(JWT_REFRESH, refreshToken)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 14)
                .path("/")
                .secure(true)
                .sameSite("None")
                .build();
    }
}
