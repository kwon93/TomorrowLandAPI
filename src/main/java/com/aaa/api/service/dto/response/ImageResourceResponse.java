package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResourceResponse {

    private String imageUrl;
    private byte[] localImageResource;
    private boolean isLocalStorage;

    @Builder
    private ImageResourceResponse(String imageUrl, byte[] imageData, boolean isLocalStorage) {
        this.imageUrl = imageUrl == null ? "noS3Url" : imageUrl;
        this.localImageResource = imageData == null ? new byte[0] : imageData;
        this.isLocalStorage = true;
    }

    public static ImageResourceResponse toLocalStorageResponse(byte[] imageData) {
        return ImageResourceResponse.builder()
                .imageData(imageData)
                .isLocalStorage(true)
                .build();
    }

    public static ImageResourceResponse toS3StorageResponse(String s3Url) {
        return ImageResourceResponse.builder()
                .imageUrl(s3Url)
                .isLocalStorage(false)
                .build();
    }
}
