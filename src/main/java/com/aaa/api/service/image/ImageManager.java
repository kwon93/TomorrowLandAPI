package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImagePath;
import com.aaa.api.service.dto.response.ImageResponse;

public interface ImageManager {

    ImageResponse uploadImage(String fileName, ImageInfo imageInfo);

    ImagePath downloadImageBy(String imagePath);

    void deleteImage(String fileName);
}
