package com.aaa.api.docs.image;

import com.aaa.api.docs.RestDocsSupport;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResponse;
import com.aaa.api.service.dto.response.ImageUrl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerDocsTest extends RestDocsSupport {

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 이미지 업로드 요청 API")
    void test1() throws Exception {
        //given
        ImageResponse response = ImageResponse.builder()
                .imagepath("image/test.png")
                .build();

        given(imageService.imageProcessing(any(ImageInfo.class))).willReturn("testUUID");
        given(imageUploader.uploadToS3(anyString(), any(ImageInfo.class))).willReturn(response);

        ResultActions result = mockMvc.perform(post("/api/image/upload")
                .with(csrf().asHeader())
                .header("originalFileName","test.png")
        );
        //then
        result.andExpect(status().isCreated())
                .andExpect(header().stringValues("ImagePath",response.getImagePath()))
                .andDo(print())
                .andDo(document("image-upload",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("originalFileName").description("업로드 이미지 원본 이름")
                        ),
                        responseFields(
                                fieldWithPath("imagePath").type(JsonFieldType.STRING).description("S3 이미지 저장 경로")
                        )

                        ));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("RestDocs: 이미지 다운로드 요청 API")
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
                .with(csrf().asHeader())
                .header("imagePath", imagePath));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value(imageUrl.getImageUrl()))
                .andDo(print())
                .andDo(document("image-download",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("imagePath").description("다운로드 대상 이미지 경로")
                        ),
                        responseFields(
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("AWS S3 객체 URL")
                        )
                        ));
    }

}
