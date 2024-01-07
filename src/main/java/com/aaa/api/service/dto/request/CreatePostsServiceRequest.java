package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePostsServiceRequest {

    private final String title;
    private final String content;
    private final PostsCategory category;
    private final Long userId;

    @Builder
    public CreatePostsServiceRequest(final String title, final String content, final PostsCategory category, final Long userId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.userId = userId;
    }

    public Posts toEntity(final Users user){
       return Posts.builder()
               .title(this.title)
               .user(user)
               .content(this.content)
               .postsCategory(this.category)
               .build();
    }
}
