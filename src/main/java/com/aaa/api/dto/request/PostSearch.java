package com.aaa.api.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.lang.Math.min;

@Getter
public class PostSearch {

    private  Integer page = 1;
    private  Integer size = 10;

    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
    }

    public long getOffset(){
        return (long) (Math.max(1, page) - 1) * size;
    }
}
