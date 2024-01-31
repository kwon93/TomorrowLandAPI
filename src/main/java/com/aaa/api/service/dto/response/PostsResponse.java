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
    private String imagePath;
    private String userName;
    private int viewCount;
    private long likeCount;


    @Builder
    public PostsResponse(final Long id, final String title, final String content, final PostsCategory category, String imagePath, int viewCount, long likeCount,String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.imagePath = imagePath;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.userName = userName;
    }

    public PostsResponse(final Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.category = entity.getCategory();
        this.viewCount = entity.getViewCount();
        this.imagePath = entity.getImagePath();
        this.userName = entity.getUser().getName();
    }


    public static PostsResponse of(final Posts entity){
        return PostsResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .userName(entity.getUser().getName())
                .build();
    }
}
