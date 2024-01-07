package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCommentResponse {

    private String content;

    @Builder
    public UpdateCommentResponse(String content) {
        this.content = content;
    }

    public static UpdateCommentResponse of(final Comment entity){
        return UpdateCommentResponse.builder()
                .content(entity.getContent())
                .build();
    }
}
