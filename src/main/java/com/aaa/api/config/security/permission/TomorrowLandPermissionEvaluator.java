package com.aaa.api.config.security.permission;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.exception.CommentNotFound;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.repository.comment.CommentRepository;
import com.aaa.api.repository.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class TomorrowLandPermissionEvaluator implements PermissionEvaluator {
    private final PostsRepository postsRepository;
    private final CommentRepository commentRepository;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        final CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();

        if (targetType.equals("Posts")) return postEvaluator(principal,targetId);
        if (targetType.equals("Comment")) return commentEvaluator(principal,targetId);

        return true;
    }

    private boolean postEvaluator(CustomUserPrincipal principal,Serializable targetId){
        final Posts posts = postsRepository.findById((Long) targetId)
                .orElseThrow(PostNotfound::new);

        if (!posts.getUserId().equals(principal.getUserId())){
            log.error("권한이 허용되지 않은 사용자입니다. 403 Error 게시물 번호: {}",targetId);
            return false;
        }
        return true;
    }
    private boolean commentEvaluator(CustomUserPrincipal principal,Serializable targetId){
        Comment comment = commentRepository.findById((Long) targetId).orElseThrow(CommentNotFound::new);

        if (!comment.getUsersId().equals(principal.getUserId())){
            log.error("권한이 허용되지 않은 사용자입니다. 403 Error 댓글 번호: {}",targetId);
            return false;
        }
        return true;
    }
}
