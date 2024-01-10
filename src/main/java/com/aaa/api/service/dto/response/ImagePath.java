package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImagePath {

    private String imagePath;

    @Builder
    public ImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
