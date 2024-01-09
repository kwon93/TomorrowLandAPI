package com.aaa.api.service.image;

import com.aaa.api.service.dto.request.ImageInfo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.servlet.ServletInputStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * S3 관련
 */
@RequiredArgsConstructor
@Component
public class S3ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3.folder}")
    private String folder;
    private final AmazonS3 amazonS3;

    public void imageToS3(final String uniqueName, final ImageInfo image) {
        final String path = folder + uniqueName;
        final ServletInputStream inputStream = image.getStream();
        final ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getContentLengthLong());

        amazonS3.putObject(bucket,path,inputStream,metadata);
    }
}
