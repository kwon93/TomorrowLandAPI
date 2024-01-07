package com.aaa.api.service.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentServiceRequest {

    private final String password;
    private final Long commentId;

    @Builder
    public DeleteCommentServiceRequest(final String password, final Long commentId) {
        this.password = password;
        this.commentId = commentId;
    }
}
