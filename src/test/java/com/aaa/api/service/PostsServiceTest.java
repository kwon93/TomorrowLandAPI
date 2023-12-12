package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.repository.PostsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostsServiceTest {

    @Autowired
    private PostsService postsService;
    @Autowired
    private  PostsRepository postsRepository;


    @AfterEach
    void tearDown() {
        postsRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("createPosts(): 작성글이 DB에 저장되어야한다.")
    void test1() throws Exception {
        //given
        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .build();
        // when
        postsService.createPosts(request);

        //then

        Posts posts = postsRepository.findAll().get(0);
        assertThat(posts).isNotNull();
        assertThat(posts.getTitle()).isEqualTo("제목");
        assertThat(posts.getContent()).isEqualTo("안녕하세요.");
    }

}