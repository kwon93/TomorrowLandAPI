package com.aaa.api.service.image;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * S3 관련
 */
public class S3ImageStorageManager{

    private static final long EXPIRATION_DATE = 3 * 60 * 1000;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.s3.folder}")
    private String folder;

//    @Override
//    public ImageResponse uploadImage(final String fileName, final ImageInfo imageInfo) {
//        final String path = uploadImageToS3Storage(fileName, imageInfo);
//        return ImageResponse.from(path);
//    }

//    @Override
//    public ImageResourceResponse downloadImage(String imagePath) {
//        GeneratePresignedUrlRequest presignedUrlRequest = preSignedUrlRequestProcessing(imagePath);
//        URL presignedUrl = amazonS3.generatePresignedUrl(presignedUrlRequest);
//
//        return ImageResourceResponse.toS3StorageResponse(presignedUrl.toString());
//    }

//    @Override
//    public void deleteImage(String fileName) throws AmazonServiceException {
//        amazonS3.deleteObject(bucket, fileName);
//    }

//    private String uploadImageToS3Storage(String fileName, ImageInfo imageInfo) {
//        final String path = folder + fileName;
//        final ServletInputStream inputStream = imageInfo.getStream();
//        final ObjectMetadata metadata = new ObjectMetadata();
//
//        metadata.setContentType(imageInfo.getContentType());
//        metadata.setContentLength(imageInfo.getContentLengthLong());
//        amazonS3.putObject(bucket, path, inputStream, metadata);
//        return path;
//    }

    private GeneratePresignedUrlRequest preSignedUrlRequestProcessing(String imagePath) {
        Date expiration = generateExpirationTime();
        return new GeneratePresignedUrlRequest(bucket, imagePath).withExpiration(expiration);
    }

    private Date generateExpirationTime() {
        long now = new Date().getTime();
        return new Date(now + S3ImageStorageManager.EXPIRATION_DATE);
    }
}
