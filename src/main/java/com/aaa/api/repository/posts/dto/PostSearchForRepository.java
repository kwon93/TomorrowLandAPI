package com.aaa.api.repository.posts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForRepository {

    private final int page;
    private final int size;
    private final int offset;
    @Builder
    public PostSearchForRepository(final int page, final int size, final int offset) {
        this.page = page;
        this.size = size;
        this.offset = offset;
    }
}
