package com.aaa.api.controller;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.service.PostsLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class PostsLikeController {

    private final PostsLikeService likeService;
    @PostMapping("like/{postsId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Void> increaseLike(@PathVariable("postsId") final Long postsId,
                                          @AuthenticationPrincipal final CustomUserPrincipal userPrincipal
                                          ){

        likeService.increase(postsId,userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("like/{postsId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Void> decreaseLike(@PathVariable("postsId") final Long postsId,
                                             @AuthenticationPrincipal final CustomUserPrincipal userPrincipal
                                             ){
        likeService.decrease(postsId, userPrincipal.getUserId());
        return ResponseEntity.noContent().build();
    }
}
