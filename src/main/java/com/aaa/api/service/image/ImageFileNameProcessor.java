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
public class ImageFileNameProcessor {
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String[] ALLOWED_EXTENSIONS = {"*.jpg", "*.jpeg", "*.png", "*.webp"};

    public String imageFileNameProcessing(final ImageInfo imageInfo) {
        this.validateImageFileName(imageInfo);
        final String extensionName = extractExtensionNameFrom(imageInfo);
        final String uuId = getUUID();
        return uuId + extensionName;
    }

    private String extractExtensionNameFrom(ImageInfo image) {
        final String originalFilename = image.getOriginalFileName();
        return EXTENSION_SEPARATOR + StringUtils.getFilenameExtension(originalFilename);
    }

    private String getUUID() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    private void validateImageFileName(final ImageInfo image) {
        final String extensionName = extractExtensionNameFrom(image);
        if (isInvalidExtension(extensionName)) {
            throw new InvalidImageExtension();
        }
    }

    private boolean isInvalidExtension(String extensionName) {
        return !PatternMatchUtils.simpleMatch(ALLOWED_EXTENSIONS, extensionName);
    }
}
