package com.aaa.api.controller.dto.request;


import com.aaa.api.service.dto.request.PostSearchForService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.lang.Math.min;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearch {

    private Integer page = 1;
    private Integer size = 10;

    @Builder
    public PostSearch(final Integer page, final Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
    }
    public long getOffset(){
        return (long) (Math.max(1, page) - 1) * size;
    }

    public PostSearchForService toServiceDto(){
        return PostSearchForService.builder()
                .page(this.page)
                .size(this.size)
                .offset(this.getOffset())
                .build();
    }

}
