package com.aaa.api.controller;

import com.aaa.api.controller.dto.request.CreateUsersRequest;
import com.aaa.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("signup")
    public ResponseEntity<String > signup(@RequestBody @Validated final CreateUsersRequest request){

        String userRole = usersService.createUser(request.toServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(userRole);
    }


}

