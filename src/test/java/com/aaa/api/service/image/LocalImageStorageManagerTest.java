package com.aaa.api.service.image;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LocalImageStorageManagerTest extends IntegrationTestSupport {

    @Value("${image.localPath}")
    private String localPath;

    @Test
    @DisplayName("uploadImage(): 정해진 경로에 binaryFile이 저장되어야 한다.")
    void test1() throws IOException {
        //given
        final String fileName = "test.png";
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setContent(new byte[8192]);

        ImageInfo imageInfo = ImageInfo.builder()
                .originalFileName("test.png")
                .request(mockRequest)
                .build();


        //when
        ImageResponse imageResponse = localImageStorageManager.uploadImage(fileName, imageInfo);

        //then
        final String filePath = localPath + fileName;

        assertThat(imageResponse.getImagePath()).isNotNull();
        assertThat(imageResponse.getImagePath()).isEqualTo(filePath);
    }


    @Test
    @DisplayName("downloadImage(): 저장된 bytes 를 가져와 응답하는데 성공해야한다.")
    void test2() {
        //given
        final String fileName = "test.png";
        final String storedFilePath = localPath + fileName;

        //when
        ImageResourceResponse response = localImageStorageManager.downloadImage(storedFilePath);

        //then
        assertThat(response.getLocalImageResource()).isInstanceOf(byte[].class);
        assertThat(response.isLocalStorage()).isTrue();
    }
}