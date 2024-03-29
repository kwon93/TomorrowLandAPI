package com.aaa.api.service.image;

import com.aaa.api.exception.InvalidImageExtension;
import com.aaa.api.service.dto.request.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String[] ALLOWED_EXTENSIONS = {"*.jpg", "*.jpeg", "*.png", "*.webp"};

    public String imageProcessing(final ImageInfo image){
        validateImage(image);
        final String originalFilename = image.getOriginalFileName();
        final String extensionName = EXTENSION_SEPARATOR + StringUtils.getFilenameExtension(originalFilename);
        final String uuId = UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, 8);
        return uuId + extensionName;
    }

    private void validateImage(final ImageInfo image) {
        final String originalFileName = image.getOriginalFileName();
        final String extensionName = EXTENSION_SEPARATOR + StringUtils.getFilenameExtension(originalFileName);

        if (!PatternMatchUtils.simpleMatch(ALLOWED_EXTENSIONS,extensionName)){
            throw new InvalidImageExtension();
        }
    }
}
