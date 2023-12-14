package com.aaa.api.dto.request;

import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePostsRequest {

    private String title;
    private String content;
    private PostsCategory postsCategory;

    @Builder
    public UpdatePostsRequest(String title, String content, PostsCategory postsCategory) {
        this.title = title;
        this.content = content;
        this.postsCategory = postsCategory;
    }
}
