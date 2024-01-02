package com.aaa.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTestController {

    @GetMapping("jwt")
    @Secured("ROLE_ADMIN")
    public String jwtTest() {
        return "jwt 인증 성공";
    }


}
