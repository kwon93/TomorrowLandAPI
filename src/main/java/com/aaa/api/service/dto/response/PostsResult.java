package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Posts;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostsResult<PostsResponse> {

    private List<PostsResponse> postsResponses;
    private Integer totalPosts;
    public PostsResult(List<PostsResponse> postsResponses, List<Posts> allPosts) {
        this.postsResponses = postsResponses;
        this.totalPosts = allPosts.size();
    }
}
