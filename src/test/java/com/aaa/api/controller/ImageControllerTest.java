package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageUrl;
import com.aaa.api.service.dto.response.ImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest extends ControllerTestSupport {


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("uploadImage(): 요청을 받고 이미지 업로드 후 http status 201 응답에 성공한다.")
    void test1() throws Exception {
        //given
        ImageResponse response = ImageResponse.builder()
                .imagepath("image/test.png")
                .build();

        given(imageService.imageProcessing(any(ImageInfo.class))).willReturn("testUUID");
        given(imageUploader.uploadToS3(anyString(), any(ImageInfo.class))).willReturn(response);

        ResultActions result = mockMvc.perform(post("/api/image/upload")
                .with(csrf())
                .header("originalFileName","test.png")
        );
        //then
        result.andExpect(status().isCreated())
                .andExpect(header().stringValues("ImagePath",response.getImagePath()))
                .andDo(print());

        verify(imageService, times(1)).imageProcessing(any(ImageInfo.class));
        verify(imageUploader, times(1)).uploadToS3(anyString(), any(ImageInfo.class));
    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getImage(): S3객체 URL 요청에 응답하고 http status 200을 반환한다.")
    void test2() throws Exception {
        //given
        final String imagePath = "image/test.png";
        final String testURL = "http://aaa-upload-image.s3.com";

        ImageUrl imageUrl = ImageUrl.builder()
                .imageUrl(testURL)
                .build();

        given(imageUploader.getPreSignedUrl(imagePath)).willReturn(imageUrl);

        // when
        ResultActions result = mockMvc.perform(get("/api/image/url")
                .with(csrf())
                .header("imagePath", imagePath));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value(imageUrl.getImageUrl()))
                .andDo(print());

        verify(imageUploader, times(1)).getPreSignedUrl(imagePath);

    }


}