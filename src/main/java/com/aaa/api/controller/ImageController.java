package com.aaa.api.controller;

import com.aaa.api.service.dto.response.ImageUrl;
import com.aaa.api.service.image.ImageService;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResponse;
import com.aaa.api.service.image.S3ImageUploader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ImageController {

    private final ImageService imageService;
    private final S3ImageUploader imageUploader;

    //REST API Resource image로 통일해야함.. 실수 TODO
    @PostMapping("image/upload")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestHeader("originalFileName")final String originalFileName,
                                                     final HttpServletRequest request) throws IOException {

        final ImageInfo imageInfo = new ImageInfo(request, originalFileName);
        final String uuId = imageService.imageProcessing(imageInfo);
        final ImageResponse imageResponse = imageUploader.uploadToS3(uuId, imageInfo);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ImagePath", imageResponse.getImagePath());

        return ResponseEntity.status(CREATED).headers(httpHeaders).body(imageResponse);
    }

    @GetMapping("image/url")
    public ResponseEntity<ImageUrl> getImage(@RequestHeader("imagePath")final String imagePath){
        ImageUrl imageUrl = imageUploader.getPreSignedUrl(imagePath);
        return ResponseEntity.ok(imageUrl);
    }
}
