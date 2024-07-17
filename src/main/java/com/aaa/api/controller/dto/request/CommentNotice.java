package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.CommentNoticeServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentNotice {

    private String commenter;
    private String postName;
    private Long postsWriterId;
    private Long postsId;

    @Builder
    public CommentNotice(String commenter, String postName, Long postsWriterId, Long postsId) {
        this.commenter = commenter;
        this.postName = postName;
        this.postsWriterId = postsWriterId;
        this.postsId = postsId;
    }

    public CommentNoticeServiceDto toServiceDto() {
        return CommentNoticeServiceDto.builder()
                .commenter(this.commenter)
                .postName(this.postName)
                .postWriterId(this.postsWriterId)
                .postsId(this.postsId)
                .build();
    }
}
