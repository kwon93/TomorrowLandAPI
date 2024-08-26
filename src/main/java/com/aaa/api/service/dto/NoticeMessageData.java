package com.aaa.api.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class NoticeMessageData {

    private String id;
    private String noticeMessage;
    private Long postWriterId;
    private Long postsId;
    private boolean read;

    @Builder
    public NoticeMessageData(final String id, final String noticeMessage, final Long postWriterId, final Long postsId, final boolean read) {
        this.id = id;
        this.noticeMessage = noticeMessage;
        this.postWriterId = postWriterId;
        this.postsId = postsId;
        this.read = read;
    }

    @JsonIgnore
    public boolean isUnread() {
        return !read;
    }

    public void markAsRead() {
        this.read = true;
    }
}
