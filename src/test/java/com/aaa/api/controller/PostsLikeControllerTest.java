package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.exception.PostNotfound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostsLikeControllerTest extends ControllerTestSupport {


    @Test
    @CustomMockUser
    @DisplayName("increaseLike(): 좋아요 요청에 성공한뒤 http status 204를 응답한다.")
    void test1() throws Exception {
        //given
        final long testId = 1L;
        doNothing().when(likeService).increase(anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/like/{postsId}",
                testId)
                .with(csrf()));
        //then
        result.andExpect(status().isNoContent())
                .andDo(print());

        verify(likeService, times(1)).increase(anyLong(), anyLong());
    }

    @Test
    @CustomMockUser
    @DisplayName("increaseLike(): 존재하지않는 게시물 좋아요 요청에 http 400을 반환한다.")
    void test2() throws Exception {
        //given
        final long testId = 1L;

        doThrow(new PostNotfound()).when(likeService).increase(anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/like/{postsId}",
                        testId)
                .with(csrf()));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("존재하지않는 게시물입니다."))
                .andDo(print());

        verify(likeService, times(1)).increase(anyLong(), anyLong());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("increaseLike(): 정보없는 회원의 좋아요 요청에 http 401을 반환한다.")
    void test3() throws Exception {
        //given
        final long testId = 1L;

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/like/{postsId}",
                        testId)
                .with(csrf()));
        //then
        result.andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @CustomMockUser
    @DisplayName("decreaseLike(): 좋아요 취소 요청에 http status 201을 반환한다. ")
    void test4() throws Exception {
        //given
        final long testId = 1L;

        doNothing().when(likeService).decrease(anyLong(),anyLong());

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/like/{postsId}",
                        testId)
                .with(csrf()));
        //then
        result.andExpect(status().isNoContent())
                .andDo(print());

        verify(likeService, times(1)).decrease(anyLong(),anyLong());

    }


}