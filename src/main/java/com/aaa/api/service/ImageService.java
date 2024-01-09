package com.aaa.api.service;

import com.aaa.api.exception.InvalidImageExtension;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResponse;
import com.aaa.api.service.image.S3ImageUploader;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.servlet.ServletInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String EXTENSION_SEPARATOR = ".";
    private final S3ImageUploader imageUploader;

    public ImageResponse upload(final ImageInfo image){
        validateImage(image);
        final String uniqueName = generateUniqueName(image);
        imageUploader.imageToS3(uniqueName, image);

        return new ImageResponse(uniqueName);

    }


    private String generateUniqueName(final ImageInfo image){
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

        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".webp");
        if (allowedExtensions.stream().noneMatch(extension -> extensionName.toLowerCase().endsWith(extension))) {
            throw new InvalidImageExtension();
        }
    }
}
