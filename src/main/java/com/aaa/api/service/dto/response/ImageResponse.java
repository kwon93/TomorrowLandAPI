package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponse {

    private String imageName;
    @Builder
    public ImageResponse(String imageName) {
        this.imageName = imageName;
    }
}