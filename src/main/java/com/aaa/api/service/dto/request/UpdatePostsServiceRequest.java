package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdatePostsServiceRequest {

    private String title;
    private String content;
    private PostsCategory category;
    private Long postsId;

    @Builder
    public UpdatePostsServiceRequest(String title, String content, PostsCategory category, Long postsId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.postsId = postsId;
    }

    public Posts updatePosts(Posts entity){
        return Posts.builder()
                .id(entity.getId())
                .title(this.title)
                .content(this.content)
                .postsCategory(this.category)
                .build();
    }
}
