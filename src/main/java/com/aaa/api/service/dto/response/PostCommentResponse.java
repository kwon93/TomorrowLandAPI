package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentResponse {

    private String content;

    @Builder
    public PostCommentResponse(final String content) {
        this.content = content;
    }

    public static PostCommentResponse of(final Comment entity){
        return PostCommentResponse.builder()
                .content(entity.getContent())
                .build();
    }
}
