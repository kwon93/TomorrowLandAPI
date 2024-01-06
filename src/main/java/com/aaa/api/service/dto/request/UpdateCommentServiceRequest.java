package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateCommentServiceRequest {

    private String content;
    private String password;
    private Long commentId;

    @Builder
    public UpdateCommentServiceRequest(String content, String password, Long commentId) {
        this.content = content;
        this.password = password;
        this.commentId = commentId;
    }

    public Comment updateComment(Comment comment){
        return Comment.builder()
                .id(comment.getId())
                .content(this.content)
                .build();
    }
}
