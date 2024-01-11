package com.aaa.api.config.security;

import com.aaa.api.domain.Posts;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final PostsRepository postsRepository;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        final CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        final Posts posts = postsRepository.findById((Long) targetId)
                .orElseThrow(PostNotfound::new);

        if (!posts.getUserId().equals(principal.getUserId())){
            log.error("권한이 허용되지 않은 사용자입니다. postId = {}",targetId);
           return false;
        }

        return true;
    }
}
