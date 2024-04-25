package com.aaa.api.service.dto.request;

import com.aaa.api.repository.posts.dto.PostSearchForRepository;
import com.aaa.api.domain.enumType.PostsCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForService {

    private final int page;
    private final int size;
    private final int offset;
    private final PostsCategory category;
    private final String searchKeyword;

    @Builder
    public PostSearchForService(final int page, final int size, final int offset, PostsCategory category, String searchKeyword) {
        this.page = page;
        this.size = size;
        this.offset = offset;
        this.category = category;
        this.searchKeyword = searchKeyword;
    }

    public PostSearchForRepository toRepository(){
        return PostSearchForRepository.builder()
                .page(this.page)
                .size(this.size)
                .offset(this.offset)
                .category(this.category)
                .searchKeyword(this.searchKeyword)
                .build();
    }
}
