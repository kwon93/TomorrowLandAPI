package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.controller.dto.request.CreateUsersRequest;
import com.aaa.api.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class UsersController {

    private final UsersService usersService;
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody @Validated final CreateUsersRequest request){
        String userName = usersService.createUser(request.toServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).body(userName);
    }

    @PatchMapping("reward/{rewardUserId}/posts/{postsId}/comment/{commentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER') && hasPermission(#postsId, 'Posts', 'PATCH')")
    public ResponseEntity<?> rewardPoint(@AuthenticationPrincipal final CustomUserPrincipal userPrincipal,
                                         @PathVariable("rewardUserId") final Long rewardUserId,
                                         @PathVariable("commentId") final Long commentId,
                                         @PathVariable("postsId") final Long postsId
                                         ){
        usersService.reward(userPrincipal.getUserId(), rewardUserId, commentId);
        return ResponseEntity.noContent().build();
    }

}

