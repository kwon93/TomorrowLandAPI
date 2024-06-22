package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.LoginRequest;
import com.aaa.api.service.AuthService;
import com.aaa.api.service.dto.response.SessionDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<Void> signIn(@RequestBody @Validated final LoginRequest loginRequest, HttpSession session) {
        SessionDataResponse sessionData = authService.login(loginRequest.toServiceDto());

        session.setAttribute("userId", sessionData.getId());
        session.setAttribute("userEmail", sessionData.getEmail());
        session.setAttribute("userName", sessionData.getName());
        session.setAttribute("userRoles", sessionData.getRole());

        return ResponseEntity.ok().build();
    }

    @PostMapping("logout")
    public ResponseEntity<Void> signOut(final HttpServletRequest request, final HttpServletResponse response){


        return ResponseEntity.noContent()
                .build();
    }

}
