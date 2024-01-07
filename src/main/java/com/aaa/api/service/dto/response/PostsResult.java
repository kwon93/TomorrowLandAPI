package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostsResult<T> {

    private List<T> postsResponses;
    public PostsResult(List<T> postsResponses) {
        this.postsResponses = postsResponses;
    }
}
