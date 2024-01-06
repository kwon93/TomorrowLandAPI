package com.aaa.api.service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentServiceRequest {

    private String password;
    private Long commentId;

    @Builder
    public DeleteCommentServiceRequest(String password, Long commentId) {
        this.password = password;
        this.commentId = commentId;
    }
}
