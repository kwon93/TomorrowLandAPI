package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.PostSearch;
import com.aaa.api.dto.request.UpdatePostsRequest;
import com.aaa.api.dto.response.PostsResponse;
import com.aaa.api.exception.PostNotfound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class PostsServiceTest extends IntegrationTestSupport {



    @Test
    @DisplayName("create(): 작성글이 DB에 저장되어야한다.")
    void test1() throws Exception {
        //given
        Users userInTest = createUserInTest();

        CreatePostsRequest request = CreatePostsRequest.builder()
                .title("제목")
                .content("안녕하세요.")
                .build();
        // when
        postsService.create(userInTest.getId(),request);

        //then

        Posts posts = postsRepository.findAll().get(0);
        assertThat(posts).isNotNull();
        assertThat(posts.getTitle()).isEqualTo("제목");
        assertThat(posts.getContent()).isEqualTo("안녕하세요.");
    }


    @Test
    @DisplayName("getAll(): 1페이지 조회(내림차순 정렬)에 성공해야한다.")
    void test2() throws Exception {
        //given
        List<Posts> postsList = IntStream.range(1, 31)
                .mapToObj(i -> Posts.builder()
                        .title("제목" + i).
                        content("내용" + i)
                        .build())
                .toList();

        postsRepository.saveAll(postsList);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostsResponse> pageOne = postsService.getAll(postSearch);

        //then
        assertThat(pageOne.size()).isEqualTo(10);
        assertThat(pageOne.get(0).getTitle()).isEqualTo("제목30");
    }


    @Test
    @DisplayName("getOne(): 단건 조회에 성공해야한다.")
    void test3() throws Exception {
        //given
        Posts savedPosts = createPostInTest();

        // when
        postsService.getOne(savedPosts.getId());

        //then
        Posts post = postsRepository.findById(savedPosts.getId()).orElseThrow(PostNotfound::new);
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }



    @Test
    @DisplayName("findPostsById(): 존재 하지 않는 게시물 조회시 PostNotFoundException() 처리")
    void test4() throws Exception {
        // when
        PostNotfound e = assertThrows(PostNotfound.class, () -> {
            postsService.getOne(777L);
        });
        //then
        assertThat(e.getMessage()).isEqualTo("존재하지않는 게시물입니다.");
    }


    @Test
    @Transactional
    @DisplayName("update(): 게시물 수정에 성공해야한다.")
    void test5() throws Exception {
        //given
        final String updateTitle = "update";
        final String updateContent = "complete";
        final PostsCategory updateCategory = PostsCategory.MEDIA;

        Posts postInTest = createPostInTest();

        UpdatePostsRequest request = UpdatePostsRequest.builder()
                .title(updateTitle)
                .content(updateContent)
                .postsCategory(updateCategory)
                .build();


        // when
        postsService.update(request, postInTest.getId());

        //then
        var post = postsRepository.findById(postInTest.getId())
                .orElseThrow(PostNotfound::new);

        assertThat(post.getTitle()).isEqualTo(updateTitle);
        assertThat(post.getContent()).isEqualTo(updateContent);
        assertThat(post.getCategory()).isEqualTo(updateCategory);
    }


    @Test
    @DisplayName("delete(): 게시물 삭제에 성공해야한다.")
    void test6() throws Exception {
        //given
        Posts postInTest = createPostInTest();
        // when
        postsService.delete(postInTest.getId());
        //then
        List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.size()).isEqualTo(0);

    }


}