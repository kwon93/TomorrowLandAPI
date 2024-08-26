package com.aaa.api.repository.posts.dto;

import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForRepository {

    private final int page;
    private final int size;
    private final int offset;
    private final PostsCategory category;
    private final String searchKeyword;

    @Builder
    public PostSearchForRepository(final int page, final int size, final int offset, PostsCategory category, String searchKeyword) {
        this.page = page;
        this.size = size;
        this.offset = offset;
        this.category = category;
        this.searchKeyword = searchKeyword;
    }
}
