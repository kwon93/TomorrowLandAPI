package com.aaa.api.controller;

import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;
import com.aaa.api.service.image.ImageFileNameProcessor;
import com.aaa.api.service.image.ImageStorageManager;
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

    private final ImageFileNameProcessor imageFileNameProcessor;
    private final ImageStorageManager imageStorageManager;

    @PostMapping("image")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestHeader("originalFileName") final String originalFileName,
                                                     final HttpServletRequest request) throws IOException {
        final ImageInfo imageInfo = ImageInfo.of(request, originalFileName);
        final String fileNameByUuid = imageFileNameProcessor.imageFileNameProcessing(imageInfo);
        final ImageResponse imageResponse = imageStorageManager.uploadImage(fileNameByUuid, imageInfo);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("ImagePath", imageResponse.getImagePath());

        return ResponseEntity.status(CREATED).headers(httpHeaders).body(imageResponse);
    }

    @GetMapping("image")
    public ResponseEntity<ImageResourceResponse> getImage(@RequestHeader("imagePath") final String imagePath) {
        final ImageResourceResponse imageResourceResponse = imageStorageManager.downloadImage(imagePath);
        return ResponseEntity.ok(imageResourceResponse);
    }
}
