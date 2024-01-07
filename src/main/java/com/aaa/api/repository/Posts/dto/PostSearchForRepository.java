package com.aaa.api.repository.Posts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForRepository {

    private final int page;
    private final int size;
    private final long offset;
    @Builder
    public PostSearchForRepository(final int page, final int size, final long offset) {
        this.page = page;
        this.size = size;
        this.offset = offset;
    }
}
