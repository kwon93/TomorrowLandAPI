package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.service.dto.request.ImageInfo;
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
    @DisplayName("uploadImage(): 요청을 받고 201 응답에 성공한다.")
    void test1() throws Exception {
        //given
        ImageResponse response = ImageResponse.builder()
                .imageName("http://S3/testImage.com/images/123")
                .build();
        given(imageService.upload(any(ImageInfo.class))).willReturn(response);
        // when
        ResultActions result = mockMvc.perform(post("/api/image").with(csrf()));
        //then
        result.andExpect(status().isCreated())
                .andExpect(header().stringValues("S3URL",response.getImageName()))
                .andDo(print());

        verify(imageService, times(1)).upload(any(ImageInfo.class));
    }

}