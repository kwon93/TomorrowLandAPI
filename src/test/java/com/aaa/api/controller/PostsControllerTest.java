package com.aaa.api.controller;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.UpdatePostsRequest;
import com.aaa.api.repository.PostsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostsRepository postsRepository;



    @Test
    @DisplayName("createPosts(): 글작성 요청에 성공해 http status code: 200 응답을 받아야 한다.")
    void test() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .build();


        // expected
        mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("안녕하세요."))
                .andDo(print());


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


        // when
        postsRepository.saveAll(postsList);

        //then
        mockMvc.perform(get("/api/posts?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andDo(print());

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

        postsRepository.saveAll(postsList);

        //then
        mockMvc.perform(get("/api/posts?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andDo(print());

    }


    @Test
    @DisplayName("getOne(): 단건조회에 성공한다.")
    void test5() throws Exception {
        //given
        Posts posts = Posts.builder()
                .title("제목")
                .content("내용")
                .build();

        Posts savedPosts = postsRepository.save(posts);

        // when then
        mockMvc.perform(get("/api/posts/{postId}",savedPosts.getId()))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("updatePosts(): 게시물 수정에 성공한다.")
    void test6() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        Posts posts = Posts.builder()
                .title("제목")
                .content("내용")
                .build();

        Posts savedPosts = postsRepository.save(posts);

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .cotent(updateContent)
                .postsCategory(updateCategory)
                .build();
        // when then
        mockMvc.perform(patch("/api/posts/{postId}",savedPosts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTitle))
                .andExpect(jsonPath("$.content").value(updateContent))
                .andDo(print());
    }

    @Test
    @DisplayName("updatePosts(): 게시물 삭제에 성공한다.")
    void test7() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        Posts posts = Posts.builder()
                .title("제목")
                .content("내용")
                .build();

        Posts savedPosts = postsRepository.save(posts);

        // when then
        mockMvc.perform(delete("/api/posts/{postId}",savedPosts.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }





}