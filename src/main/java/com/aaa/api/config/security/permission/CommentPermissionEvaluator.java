package com.aaa.api.config.security.permission;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Comment;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class CommentPermissionEvaluator implements PermissionEvaluator {

    private final CommentRepository commentRepository;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        Comment comment = commentRepository.findById((Long) targetId).orElseThrow(CommentNotFound::new);
        if (!comment.getUsers().getId().equals(principal.getUserId())){
            log.error("권한이 허용되지 않은 사용자입니다. 403 commentId: {}",targetId);
            return false;
        }
        return true;
    }
}
