package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.PostSearch;
import com.aaa.api.dto.request.UpdatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.exception.PostNotfound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PostsControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("createPosts(): 글작성 요청에 성공해 http status code: 201 응답을 받아야 한다.")
    void test() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .build();

        PostsResponse response = PostsResponse.builder()
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .build();

        given(postsService.create(any(CreatePostsRequest.class))).willReturn(response);


        // expected
        mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("제목"))
                .andDo(print());

        verify(postsService, times(1)).create(refEq(request));
    }

    @Test
    @DisplayName("createPosts(): 제목, 내용이 없을시 ExceptionResponse를 반환 해야한다.")
    void test2() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("")
                .content("")
                .build();


        // expected
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andDo(print());
    }


    @Test
    @DisplayName("getAllPosts(): 1페이지 조회에 성공한다.")
    void test3() throws Exception {
        //given
        List<Posts> postsList = IntStream.range(1, 31)
                .mapToObj(i -> Posts.builder()
                        .title("제목" + i).
                        content("내용" + i)
                        .build())
                .toList();


        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        mockMvc.perform(get("/api/posts?page=1&size=10"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postsService, times(1)).getAll(refEq(postSearch));

    }

    @Test
    @DisplayName("getAllPosts(): 0페이지 조회시 1페이지 조회에 성공한다.")
    void test4() throws Exception {
        //given
        List<Posts> postsList = IntStream.range(1, 31)
                .mapToObj(i -> Posts.builder()
                        .title("제목" + i).
                        content("내용" + i)
                        .build())
                .toList();

        PostSearch postSearch = PostSearch.builder()
                .page(0)
                .build();

        //then
        mockMvc.perform(get("/api/posts?page=0"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postsService, times(1)).getAll(refEq(postSearch));

    }


    @Test
    @DisplayName("getOne(): 단건조회에 성공해 http status 200 응답을 받아야한다..")
    void test5() throws Exception {
        //given
        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .category(PostsCategory.DEV)
                .build();

        given(postsService.getOne(response.getId())).willReturn(response);

        // when then
        mockMvc.perform(get("/api/posts/{postId}",response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.category").value(PostsCategory.DEV.toString()))
                .andDo(print());

        verify(postsService, times(1)).getOne(response.getId());
    }

    @Test
    @DisplayName("updatePosts(): 게시물 수정에 성공해 http status 200 응답을 받아야한다.")
    void test6() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .cotent(updateContent)
                .postsCategory(updateCategory)
                .build();

        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title(updateTitle)
                .content(updateContent)
                .category(updateCategory)
                .build();

        given(postsService.update(any(UpdatePostsRequest.class),any(Long.class))).willReturn(response);

        // when then
        mockMvc.perform(patch("/api/posts/{postId}",response.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTitle))
                .andExpect(jsonPath("$.content").value(updateContent))
                .andDo(print());

        verify(postsService, times(1))
                .update(refEq(request,"fieldToIgnore"),eq(response.getId()));
    }

    @Test
    @DisplayName("updatePosts(): 게시물 삭제에 성공해 204응답을 받아야한다.")
    void test7() throws Exception {
        //given
        final Long id = 1L;

        // when then
        mockMvc.perform(delete("/api/posts/{postId}",id))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(postsService, times(1)).delete(id);
    }

}