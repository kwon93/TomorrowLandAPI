package com.aaa.api.controller.dto.request;


import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.PostSearchForService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearch {

    private Integer page = 1;
    private Integer size = 10;
    private PostsCategory category;
    private String searchKeyword;

    @Builder
    private PostSearch(final Integer page, final Integer size, PostsCategory category, String searchKeyword) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
        this.category = category;
        this.searchKeyword = searchKeyword;
    }

    public int getOffset(){
        return (Math.max(1, page) - 1) * size;
    }

    public PostSearchForService toServiceDto(){
        return PostSearchForService.builder()
                .page(this.page)
                .size(this.size)
                .offset(this.getOffset())
                .category(this.category)
                .searchKeyword(this.searchKeyword)
                .build();
    }

}
