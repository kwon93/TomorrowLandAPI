package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostsResponse {

    private Long id;
    private String title;
    private String content;
    private PostsCategory category;


    @Builder
    public PostsResponse(final Long id,final String title, final String content, final PostsCategory category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public PostsResponse(final Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
    }


    public static PostsResponse of(final Posts entity){
        return PostsResponse.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .build();
    }
}
