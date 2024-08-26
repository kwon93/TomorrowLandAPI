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
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;

class LocalImageStorageManagerTest extends IntegrationTestSupport {

    @Value("${image.localPath}")
    private String LOCAL_PATH;
    private static final String FILE_NAME = "test.png";

    @Test
    @DisplayName("uploadImage(): 정해진 경로에 binaryFile이 저장되어야 한다.")
    void test1() throws IOException {
        //given
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setContent(new byte[8192]);

        ImageInfo imageInfo = ImageInfo.builder()
                .originalFileName("test.png")
                .request(mockRequest)
                .build();


        //when
        ImageResponse imageResponse = localImageStorageManager.uploadImage(FILE_NAME, imageInfo);

        //then
        final String filePath = LOCAL_PATH + FILE_NAME;

        assertThat(imageResponse.getImagePath()).isNotNull();
        assertThat(imageResponse.getImagePath()).isEqualTo(filePath);
    }


    @Test
    @DisplayName("downloadImage(): 저장된 bytes 를 가져와 응답하는데 성공해야한다.")
    void test2() {
        //given
        final String storedFilePath = LOCAL_PATH + FILE_NAME;

        //when
        ImageResourceResponse response = localImageStorageManager.downloadImage(storedFilePath);

        //then
        assertThat(response.getLocalImageResource()).isInstanceOf(byte[].class);
        assertThat(response.isLocalStorage()).isTrue();
    }


    @Test
    @DisplayName("deleteImage(): 저장된 파일 삭제에 성공해야한다.")
    void test3() {
        //given
        final String imagePath = LOCAL_PATH + FILE_NAME;

        //when
        localImageStorageManager.deleteImage(imagePath);

        //then
        Path path = get(imagePath);
        boolean isExistFile = Files.exists(path);
        assertThat(isExistFile).isFalse();
    }
}