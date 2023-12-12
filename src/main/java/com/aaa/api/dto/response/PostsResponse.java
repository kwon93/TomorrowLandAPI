package com.aaa.api.dto.response;

import com.aaa.api.domain.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsResponse {

    private String title;
    private String content;


    @Builder
    public PostsResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostsResponse of(Posts entity){
        return PostsResponse.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }
}
