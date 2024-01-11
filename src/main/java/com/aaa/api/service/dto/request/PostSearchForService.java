package com.aaa.api.service.dto.request;

import com.aaa.api.repository.posts.dto.PostSearchForRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForService {

    private final int page;
    private final int size;
    private final long offset;

    @Builder
    public PostSearchForService(final int page, final int size, final long offset) {
        this.page = page;
        this.size = size;
        this.offset = offset;
    }

    public PostSearchForRepository toRepository(){
        return PostSearchForRepository.builder()
                .page(this.page)
                .size(this.size)
                .build();
    }
}
