package com.aaa.api.controller;

import com.aaa.api.service.dto.response.ImagePath;
import com.aaa.api.service.image.ImageService;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResponse;
import com.aaa.api.service.image.S3ImageManager;
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
    private final S3ImageManager imageUploader;

    @PostMapping("image/upload")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestHeader("originalFileName") final String originalFileName,
                                                     final HttpServletRequest request) throws IOException {
        final ImageInfo imageInfo = ImageInfo.of(request, originalFileName);
        final String fileNameByUuid = imageService.imageFileNameProcessing(imageInfo);
        final ImageResponse imageResponse = imageUploader.uploadImage(fileNameByUuid, imageInfo);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ImagePath", imageResponse.getImagePath());

        return ResponseEntity.status(CREATED).headers(httpHeaders).body(imageResponse);
    }

    @GetMapping("image/url")
    public ResponseEntity<ImagePath> getImage(@RequestHeader("imagePath") final String imagePath) {
        ImagePath imagePath1 = imageUploader.downloadImageBy(imagePath);
        return ResponseEntity.ok(imagePath1);
    }
}
