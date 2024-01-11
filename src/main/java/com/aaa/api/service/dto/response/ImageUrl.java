package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUrl {

    private String imageUrl;

    @Builder
    public ImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
