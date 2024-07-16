package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.CommentNoticeServiceDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentNotice {

    private String commenter;
    private String postName;
    private Long postsWriterId;
    private Long postsId;

    public CommentNoticeServiceDto toServiceDto() {
        return CommentNoticeServiceDto.builder()
                .commenter(this.commenter)
                .postName(this.postName)
                .postWriterId(this.postsWriterId)
                .postsId(this.postsId)
                .build();
    }
}
