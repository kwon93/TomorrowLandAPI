package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.CreateUsersRequest;
import com.aaa.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody @Validated final CreateUsersRequest request){

        String userRole = usersService.createUser(request.toServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(userRole);
    }


    @PatchMapping("reward/{rewardUserId}")
    public ResponseEntity<?> rewardPoint(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal,
                                         @PathVariable("rewardUserId") final Long rewardUserId){
        usersService.reward(userPrincipal.getUserId(), rewardUserId);
        return ResponseEntity.noContent().build();
    }


}

