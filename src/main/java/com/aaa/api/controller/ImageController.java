package com.aaa.api.controller;

import com.aaa.api.service.ImageService;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ImageController {

    private final ImageService imageService;
    @PostMapping("image")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestHeader("originalName")final String originalFileName,
                                                     final HttpServletRequest request) throws IOException {

        ImageResponse imageResponse = imageService.upload(new ImageInfo(request, originalFileName));
        String imageName = imageResponse.getImageName();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("S3URL", imageName);

        return ResponseEntity.status(CREATED).headers(httpHeaders).body(imageResponse);
    }

}
