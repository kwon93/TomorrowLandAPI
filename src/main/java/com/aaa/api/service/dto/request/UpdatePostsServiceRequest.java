package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdatePostsServiceRequest {

    private final String title;
    private final String content;
    private final PostsCategory category;
    private final Long postsId;

    @Builder
    public UpdatePostsServiceRequest(final String title,
                                     final String content,
                                     final PostsCategory category,
                                     final Long postsId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.postsId = postsId;
    }

    public Posts updatePosts(final Posts entity){
        return Posts.builder()
                .id(entity.getId())
                .title(this.title)
                .content(this.content)
                .postsCategory(this.category)
                .user(entity.getUser())
                .build();
    }
}
