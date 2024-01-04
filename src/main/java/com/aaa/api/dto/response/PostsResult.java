package com.aaa.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsResult<T> {

    private List<T> postsResponses;
    public PostsResult(List<T> postsResponses) {
        this.postsResponses = postsResponses;
    }
}
