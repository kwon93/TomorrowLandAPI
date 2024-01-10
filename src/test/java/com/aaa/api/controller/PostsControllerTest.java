package com.aaa.api.controller;

import com.aaa.api.ControllerTestSupport;
import com.aaa.api.config.CustomMockUser;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.controller.dto.request.CreatePostsRequest;
import com.aaa.api.controller.dto.request.PostSearch;
import com.aaa.api.controller.dto.request.UpdatePostsRequest;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import com.aaa.api.service.dto.request.PostSearchForService;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import com.aaa.api.service.dto.response.PostsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PostsControllerTest extends ControllerTestSupport {

    @Test
    @CustomMockUser
    @DisplayName("createPosts(): 글작성 요청에 성공해 http status code: 201 응답을 받아야 한다.")
    void test() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .build();

        PostsResponse response = PostsResponse.builder()
                .title("제목")
                .content("안녕하세요.")
                .category(PostsCategory.DEV)
                .build();

        given(postsService.create(any(CreatePostsServiceRequest.class))).willReturn(response);

        // expected
        mockMvc.perform(post("/api/posts")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("제목"))
                .andDo(print());

        verify(postsService, times(1)).create(any(CreatePostsServiceRequest.class));
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("createPosts(): 제목, 내용이 없을시 ExceptionResponse를 반환 해야한다.")
    void test2() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("")
                .content("")
                .category(PostsCategory.DEV)
                .build();


        // expected
        mockMvc.perform(post("/api/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getAllPosts(): 1페이지 조회에 성공한다.")
    void test3() throws Exception {
        //given
        List<Posts> postsList = IntStream.range(1, 31)
                .mapToObj(i -> Posts.builder()
                        .title("제목" + i).
                        content("내용" + i)
                        .build())
                .toList();


        PostSearchForService postSearch = PostSearchForService.builder()
                .page(1)
                .build();

        // when
        mockMvc.perform(get("/api/posts?page=1&size=10").with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postsService, times(1)).getAll(any(PostSearchForService.class));

    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("getAllPosts(): 0페이지 조회시 1페이지 조회에 성공한다.")
    void test4() throws Exception {
        //given
        List<Posts> postsList = IntStream.range(1, 31)
                .mapToObj(i -> Posts.builder()
                        .title("제목" + i).
                        content("내용" + i)
                        .build())
                .toList();

        PostSearchForService postSearch = PostSearchForService.builder()
                .page(0)
                .build();

        //then
        mockMvc.perform(get("/api/posts?page=0").with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(postsService, times(1)).getAll(any(PostSearchForService.class));

    }


    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
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
        mockMvc.perform(get("/api/posts/{postId}",response.getId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.category").value(PostsCategory.DEV.toString()))
                .andDo(print());

        verify(postsService, times(1)).getOne(response.getId());
    }

    @Test
    @WithMockUser(username = "kdh93@naver.com", password = "kdh1234", roles = "{ROLE_USER}")
    @DisplayName("updatePosts(): 게시물 수정에 성공해 http status 200 응답을 받아야한다.")
    void test6() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .content(updateContent)
                .postsCategory(updateCategory)
                .build();

        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title(updateTitle)
                .content(updateContent)
                .category(updateCategory)
                .build();

        given(postsService.update(any(UpdatePostsServiceRequest.class))).willReturn(response);

        // when then
        mockMvc.perform(patch("/api/posts/{postId}",response.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTitle))
                .andExpect(jsonPath("$.content").value(updateContent))
                .andDo(print());

        verify(postsService, times(1))
                .update(any(UpdatePostsServiceRequest.class));
    }

    @Test
    @CustomMockUser
    @DisplayName("deletePosts(): 게시물을 작성한 사용자가 게시물 삭제에 성공해 204응답을 받아야한다.")
    void test7() throws Exception {
        //given
        final Long id = 1L;

        // when then
        mockMvc.perform(delete("/api/posts/{postId}",id).with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(postsService, times(1)).delete(id);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("deletePosts(): 게시물을 작성한 사용자가 아닐경우 http status 401 응답을 받아야한다.")
    void test8() throws Exception {
        //given
        final Long id = 1L;

        // when then
        mockMvc.perform(delete("/api/posts/{postId}",id).with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("updatePosts(): 게시물 작성자가아닌 다른 사용자의 수정 요청에는 http status 401 응답을 받아야한다.")
    void test9() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .content(updateContent)
                .postsCategory(updateCategory)
                .build();

        PostsResponse response = PostsResponse.builder()
                .id(1L)
                .title(updateTitle)
                .content(updateContent)
                .category(updateCategory)
                .build();

        given(postsService.update(any(UpdatePostsServiceRequest.class))).willReturn(response);

        // when then
        mockMvc.perform(patch("/api/posts/{postId}",response.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }



}