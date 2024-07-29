package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.dto.response.SessionDataResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<Void> signIn(@RequestBody @Validated final LoginRequest loginRequest, HttpSession session) {
        SessionDataResponse sessionData = authService.processingUserSessionBy(loginRequest.toServiceDto());

        session.setAttribute("userEmail", sessionData.getEmail());
        session.setAttribute("userRoles", sessionData.getRole().toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("logout")
    public ResponseEntity<Void> signOut(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent()
                .build();
    }

}
