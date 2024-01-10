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

    private final String contentType;
    private final long contentLengthLong;
    private final ServletInputStream stream;
    private final String originalFileName;

    @Builder
    public ImageInfo(HttpServletRequest request, String originalFileName) throws IOException {
        this.contentType = request.getContentType();
        this.contentLengthLong = request.getContentLengthLong();
        this.stream = request.getInputStream();
        this.originalFileName = originalFileName;
    }

}
