package com.aaa.api.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostsRequest {

    private String title;
    private String content;

    @Builder
    public CreatePostsRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
