package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostsResult<PostsResponse> {

    private List<PostsResponse> postsResponses;
    public PostsResult(List<PostsResponse> postsResponses) {
        this.postsResponses = postsResponses;
    }
}
