package com.aaa.api.service.dto.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public class ImageInfo {

    private String contentType;
    private long contentLengthLong;
    private ServletInputStream stream;
    private String originalFileName;

    @Builder
    private ImageInfo(HttpServletRequest request, String originalFileName) throws IOException {
        this.contentType = request.getContentType();
        this.contentLengthLong = request.getContentLengthLong();
        this.stream = request.getInputStream();
        this.originalFileName = originalFileName;
    }

    public static ImageInfo of(HttpServletRequest request, String originalFileName) throws IOException {
        return ImageInfo.builder()
                .originalFileName(originalFileName)
                .request(request)
                .build();
    }

    public byte[] extractByteImage() throws IOException {
        return this.stream.readAllBytes();
    }



}
