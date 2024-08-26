package com.aaa.api.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateCommentNotice {

    @Getter
    private String noticeId;

    public UpdateCommentNotice(String noticeId) {
        this.noticeId = noticeId;
    }
}
