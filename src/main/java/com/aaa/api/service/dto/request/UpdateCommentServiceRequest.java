package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateCommentServiceRequest {

    private final String content;
    private final String password;
    private final Long commentId;

    @Builder
    private UpdateCommentServiceRequest(final String content, final String password, final Long commentId) {
        this.content = content;
        this.password = password;
        this.commentId = commentId;
    }

    public Comment updateComment(final Comment comment){
        return Comment.builder()
                .id(comment.getId())
                .content(this.content)
                .build();
    }
}
