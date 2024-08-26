package com.aaa.api.service.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentNoticeServiceDto {

    private final String commenter;
    private final String postName;
    private final Long postWriterId;
    private final Long postsId;

    @Builder
    public CommentNoticeServiceDto(String commenter, String postName, Long postWriterId, Long postsId) {
        this.commenter = commenter;
        this.postName = postName;
        this.postWriterId = postWriterId;
        this.postsId = postsId;
    }

}
