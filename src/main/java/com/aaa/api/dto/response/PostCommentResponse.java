package com.aaa.api.dto.response;

import com.aaa.api.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCommentResponse {

    private String name;
    private String password;
    private String content;

    @Builder
    public PostCommentResponse(String name, String password, String content) {
        this.name = name;
        this.password = password;
        this.content = content;
    }

    public static PostCommentResponse of(Comment entity){
        return PostCommentResponse.builder()
                .content(entity.getContent())
                .name(entity.getUsername())
                .build();
    }
}
