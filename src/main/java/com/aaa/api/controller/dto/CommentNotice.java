package com.aaa.api.controller.dto;

import lombok.Getter;

@Getter
public class CommentNotice {

    private String commenter;
    private String postName;

    public String notice (){
        return this.postName + " 글에 "+ this.commenter+"님이 댓글을 달았습니다.";
    }

}
