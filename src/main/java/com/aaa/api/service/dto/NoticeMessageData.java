package com.aaa.api.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoticeMessageData {

    @Getter
    private String id;
    @Getter
    private String noticeMessage;
    @Getter
    private Long postWriterId;
    private Long postsId;
    private boolean read;

    @Builder
    public NoticeMessageData(String id, String noticeMessage, Long postWriterId, Long postsId, boolean read) {
        this.id = id;
        this.noticeMessage = noticeMessage;
        this.postWriterId = postWriterId;
        this.postsId = postsId;
        this.read = read;
    }

    public boolean isUnread() {
        return !read;
    }

    public void markAsRead() {
        this.read = true;
    }
}
