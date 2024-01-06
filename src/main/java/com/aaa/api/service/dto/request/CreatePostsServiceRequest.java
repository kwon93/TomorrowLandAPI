package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePostsServiceRequest {

    private String title;
    private String content;
    private PostsCategory category;
    private Long userId;

    @Builder
    public CreatePostsServiceRequest(String title, String content, PostsCategory category, Long userId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.userId = userId;
    }

    public Posts toEntity(Users user){
       return Posts.builder()
               .title(this.title)
               .user(user)
               .content(this.content)
               .postsCategory(this.category)
               .build();
    }
}
