package com.aaa.api.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class NoticeMessageDatas {

    @Getter
    private List<NoticeMessageData> noticeMessageDatas;
    private int noticeCounts;

    @Builder
    public NoticeMessageDatas(List<NoticeMessageData> noticeMessageDatas, int noticeCounts) {
        this.noticeMessageDatas = noticeMessageDatas;
        this.noticeCounts = noticeMessageDatas.size();
    }
}
