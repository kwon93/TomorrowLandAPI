package com.aaa.api.docs.like;

import com.aaa.api.config.CustomMockUser;
import com.aaa.api.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostsLikeControllerDocsTest extends RestDocsSupport {

    @Test
    @CustomMockUser
    @DisplayName("RestDocs: 좋아요 추천 API")
    void test1() throws Exception {
        //given
        final long testId = 1L;
        doNothing().when(likeService).likeIncreaseProcess(anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/like/{postsId}",
                        testId)
                .with(csrf().asHeader()));
        //then
        result.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("like-increase",
                        preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postsId").description("추천 게시물 번호")
                        )
                        ));
    }

    @Test
    @CustomMockUser
    @DisplayName("RestDocs: 좋아요 차감 API")
    void test4() throws Exception {
        //given
        final long testId = 1L;

        doNothing().when(likeService).decrease(anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/like/{postsId}",
                        testId)
                .with(csrf().asHeader()));
        //then
        result.andExpect(status().isNoContent())
                .andDo(print())
                        .andDo(MockMvcRestDocumentation.document("like-decrease",
                                preprocessRequest(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN")),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postsId").description("추천 차감 게시물 번호")
                                )
                                ));


    }
}
