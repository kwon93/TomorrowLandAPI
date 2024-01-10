package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImagePath;
import com.aaa.api.service.dto.response.ImageResponse;
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
public class S3ImageUploader {


    private static final long EXPIRATION_DATE = 3 * 60 * 1000;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3.folder}")
    private String folder;
    private final AmazonS3 amazonS3;

    public ImageResponse uploadToS3(final String uniqueName, final ImageInfo image) {
        final String path = folder + uniqueName;
        final ServletInputStream inputStream = image.getStream();
        final ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getContentLengthLong());
        amazonS3.putObject(bucket,path,inputStream,metadata);

        return ImageResponse.builder()
                .imagepath(path)
                .build();
    }

    public ImagePath getPreSignedUrl(String imagePath){
        Date expiration = generateExpirationTime(EXPIRATION_DATE);
        GeneratePresignedUrlRequest presignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket,imagePath)
                .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(presignedUrlRequest);
        return ImagePath.builder()
                .imagePath(url.toString())
                .build();
    }

    private Date generateExpirationTime(long expirationTime){
        long now = new Date().getTime();
        return new Date(now + expirationTime);
    }
}
