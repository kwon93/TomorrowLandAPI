package com.aaa.api.repository.Posts.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForRepository {

    private int page;
    private int size;
    private long offset;
    @Builder
    public PostSearchForRepository(int page, int size, long offset) {
        this.page = page;
        this.size = size;
        this.offset = offset;
    }
}
