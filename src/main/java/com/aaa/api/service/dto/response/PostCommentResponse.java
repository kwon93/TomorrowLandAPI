package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCommentResponse {

    private String username;
    private String password;
    private String content;

    @Builder
    public PostCommentResponse(String username, String password, String content) {
        this.username = username;
        this.password = password;
        this.content = content;
    }

    public static PostCommentResponse of(Comment entity){
        return PostCommentResponse.builder()
                .content(entity.getContent())
                .username(entity.getUsername())
                .build();
    }
}
