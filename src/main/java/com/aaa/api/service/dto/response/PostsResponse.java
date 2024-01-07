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
    private int viewCount;


    @Builder
    public PostsResponse(final Long id, final String title, final String content, final PostsCategory category, int viewCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.viewCount = viewCount;
    }

    public PostsResponse(final Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
        this.viewCount = entity.getViewCount();
    }


    public static PostsResponse of(final Posts entity){
        return PostsResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .viewCount(entity.getViewCount())
                .build();
    }
}
