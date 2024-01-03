package com.aaa.api.dto.response;

import com.aaa.api.domain.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private String name;
    private String password;
    private String content;

    @Builder
    public CommentResponse(String name, String password, String content) {
        this.name = name;
        this.password = password;
        this.content = content;
    }

    public static CommentResponse of(Comment entity){
        return CommentResponse.builder()
                .content(entity.getContent())
                .name(entity.getUsername())
                .build();
    }
}
