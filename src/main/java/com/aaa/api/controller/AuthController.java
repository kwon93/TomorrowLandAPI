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
    public ResponseEntity<JwtToken> signIn(@RequestBody @Validated final LoginRequest loginRequest,
                                           final HttpServletResponse response
                                           ) {
        final JwtToken jwtToken = authService.login(loginRequest.toServiceDto());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + jwtToken.getAccessToken());
        httpHeaders.add(JWT_REFRESH, jwtToken.getRefreshToken());

        Cookie refreshCookie = getRefreshCookie(jwtToken.getRefreshToken());
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwtToken);
    }

    @PatchMapping("reissue")
    public ResponseEntity<String> reIssueRefreshToken(final HttpServletRequest request,
                                                      final HttpServletResponse response
                                                      ){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JWT_REFRESH))
                .findFirst()
                .get()
                .getValue();
        final String reIssueAccessToken = authService.reissueAccessToken(refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWT_AUTH_HEADER, GRANT_TYPE + reIssueAccessToken);
        Cookie refreshCookie = getRefreshCookie(refreshToken);
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(httpHeaders)
                .body("refresh");
    }

    private static Cookie getRefreshCookie(String refreshToken) {
        Cookie refreshCookie = new Cookie(JWT_REFRESH, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(60 * 60 * 24 * 14);
        refreshCookie.setPath("/");
        return refreshCookie;
    }
}
