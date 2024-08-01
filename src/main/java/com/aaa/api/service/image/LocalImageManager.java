package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImagePath;
import com.aaa.api.service.dto.response.ImageResponse;
import org.springframework.stereotype.Service;

@Service
public class LocalImageManager implements ImageManager{

    @Override
    public ImageResponse uploadImage(String fileName, ImageInfo imageInfo) {
        return null;
    }

    @Override
    public ImagePath downloadImageBy(String imagePath) {
        return null;
    }

    @Override
    public void deleteImage(String fileName) {

    }
}
