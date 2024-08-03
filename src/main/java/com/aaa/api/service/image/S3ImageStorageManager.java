package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.servlet.ServletInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;

/**
 * S3 관련
 */
@RequiredArgsConstructor
@Component
public class S3ImageStorageManager implements ImageStorageManager {

    private static final long EXPIRATION_DATE = 3 * 60 * 1000;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3.folder}")
    private String folder;
    private final AmazonS3 amazonS3;

    @Override
    public ImageResponse uploadImage(final String fileName, final ImageInfo imageInfo) {
        final String path = uploadImageToS3Storage(fileName, imageInfo);
        return ImageResponse.from(path);
    }

    @Override
    public ImageResourceResponse downloadImage(String imagePath) {
        GeneratePresignedUrlRequest presignedUrlRequest = preSignedUrlRequestProcessing(imagePath);
        URL presignedUrl = amazonS3.generatePresignedUrl(presignedUrlRequest);

        return ImageResourceResponse.toS3StorageResponse(presignedUrl.toString());
    }

    @Override
    public void deleteImage(String fileName) throws AmazonServiceException {
        amazonS3.deleteObject(bucket, fileName);
    }

    private String uploadImageToS3Storage(String fileName, ImageInfo imageInfo) {
        final String path = folder + fileName;
        final ServletInputStream inputStream = imageInfo.getStream();
        final ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(imageInfo.getContentType());
        metadata.setContentLength(imageInfo.getContentLengthLong());
        amazonS3.putObject(bucket, path, inputStream, metadata);
        return path;
    }

    private GeneratePresignedUrlRequest preSignedUrlRequestProcessing(String imagePath) {
        Date expiration = generateExpirationTime();
        return new GeneratePresignedUrlRequest(bucket, imagePath).withExpiration(expiration);
    }

    private Date generateExpirationTime() {
        long now = new Date().getTime();
        return new Date(now + S3ImageStorageManager.EXPIRATION_DATE);
    }
}
