package com.aaa.api.dto.response;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsResponse {

    private Long id;
    private String title;
    private String content;
    private PostsCategory category;


    @Builder
    public PostsResponse(Long id,String title, String content, PostsCategory category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public static PostsResponse of(Posts entity){
        return PostsResponse.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .build();
    }
}
