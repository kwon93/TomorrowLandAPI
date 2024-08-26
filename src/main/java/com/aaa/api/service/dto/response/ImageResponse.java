package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponse {

    private String imagePath;

    private ImageResponse(String imagePath) {
        this.imagePath = imagePath;
    }

    public static ImageResponse from(String imagePath) {
        return new ImageResponse(imagePath);
    }

}