package com.aaa.api.service.image;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.exception.InvalidImageExtension;
import com.aaa.api.service.dto.request.ImageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageServiceTest extends IntegrationTestSupport {

    @Test
    @DisplayName("imageProcessing(): imageInfoDTO를 통해 uuid 생성에 성공한다. ")
    void test1() throws IOException {
        //given
        final String contentType = "image/png";
        final String originalFileName = "test.png";
        final byte[] mockByte = new byte[]{Mockito.anyByte()};

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(contentType);
        request.setContent(mockByte);
        ImageInfo imageInfo = ImageInfo.of(request, originalFileName);

        // when
        String uuid = imageService.imageFileNameProcessing(imageInfo);

        //then
        assertThat(uuid).isInstanceOf(String.class);
    }


    @Test
    @DisplayName("imageProcessing(): 허용되지않은 이미지 확장자일 경우 InvalidImageException을 반환한다.")
    void test2() throws IOException {
        //given
        final String contentType = "image/png";
        final String originalFileName = "invalid.exe";
        final byte[] mockByte = new byte[]{Mockito.anyByte()};

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType(contentType);
        request.setContent(mockByte);
        ImageInfo imageInfo = ImageInfo.of(request, originalFileName);

        // when
        assertThatThrownBy(() -> imageService.imageFileNameProcessing(imageInfo))
                .isInstanceOf(InvalidImageExtension.class)
                .hasMessage("png,jpg,jpeg,webp 외의 확장자명 오류");

    }
}