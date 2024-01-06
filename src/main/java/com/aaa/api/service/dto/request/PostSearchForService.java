package com.aaa.api.service.dto.request;

import com.aaa.api.repository.Posts.dto.PostSearchForRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchForService {

    private int page;
    private int size;
    private long offset;

    @Builder
    public PostSearchForService(int page, int size, long offset) {
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
