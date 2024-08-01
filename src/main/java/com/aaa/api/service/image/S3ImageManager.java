package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImagePath;
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
public class S3ImageManager implements ImageManager{

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
        final String path = folder + fileName;
        final ServletInputStream inputStream = imageInfo.getStream();
        final ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(imageInfo.getContentType());
        metadata.setContentLength(imageInfo.getContentLengthLong());
        amazonS3.putObject(bucket,path,inputStream,metadata);

        return ImageResponse.builder()
                .imagepath(path)
                .build();
    }

    @Override
    public ImagePath downloadImageBy(String imagePath){
        Date expiration = generateExpirationTime();
        GeneratePresignedUrlRequest presignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket,imagePath)
                .withExpiration(expiration);
        URL url = amazonS3.generatePresignedUrl(presignedUrlRequest);

        return ImagePath.builder()
                .imageUrl(url.toString())
                .build();
    }

    @Override
    public void deleteImage(String fileName) throws AmazonServiceException {
        amazonS3.deleteObject(bucket, fileName);
    }

    private Date generateExpirationTime(){
        long now = new Date().getTime();
        return new Date(now + S3ImageManager.EXPIRATION_DATE);
    }
}
