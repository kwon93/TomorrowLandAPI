package com.aaa.api.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class NoticeMessageData {

    private final String noticeMessage;
    @Getter
    private final Long postWriterId;
    private final Long postsId;
}
