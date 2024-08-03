package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;

import java.io.IOException;

public interface ImageStorageManager {

    ImageResponse uploadImage(String fileName, ImageInfo imageInfo) throws IOException;

    ImageResourceResponse downloadImage(String imagePath);

    void deleteImage(String fileName);
}
