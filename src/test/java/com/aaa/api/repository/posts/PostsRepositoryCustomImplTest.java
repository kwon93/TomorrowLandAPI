package com.aaa.api.repository.posts;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.repository.posts.dto.PostSearchForRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PostsRepositoryCustomImplTest extends IntegrationTestSupport {


    @Test
    @DisplayName("getList(): 조회요청에 Category MEDIA 선택시 MEDIA Category글만 가져오는데 성공한다.")
    void test1() {
        //given
        Users userInTest = createUserInTest();
        List<Posts> posts = LongStream.range(0, 11).mapToObj(i ->
                Posts.builder()
                        .user(userInTest)
                        .title("testPosts" + i)
                        .content("testContent" + i)
                        .postsCategory(PostsCategory.MEDIA)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()
        ).toList();

        postsRepository.saveAll(posts);

        List<Posts> devPosts = LongStream.range(0, 5).mapToObj(i ->
                Posts.builder()
                        .user(userInTest)
                        .title("testPosts" + i)
                        .content("testContent" + i)
                        .postsCategory(PostsCategory.DEV)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()
        ).toList();

        postsRepository.saveAll(devPosts);

        PostSearchForRepository requestDTO = PostSearchForRepository.builder()
                .page(1)
                .size(10)
                .offset(1)
                .category(PostsCategory.MEDIA)
                .build();
        // when
        List<Posts> postsByCategorySearch = postsRepository.getList(requestDTO);

        //then
        assertThat(postsByCategorySearch.size()).isEqualTo(10);
        assertThat(postsByCategorySearch.get(0).getTitle()).isEqualTo("testPosts9");
        assertThat(postsByCategorySearch.get(3).getCategory()).isEqualTo(PostsCategory.MEDIA);
        assertThat(postsByCategorySearch.get(5).getCategory()).isEqualTo(PostsCategory.MEDIA);
    }

    @Test
    @DisplayName("getList(): Category가 null일 경우 모든 카테고리의 글을 조회한다.")
    void test2() {
        //given
        Users userInTest = createUserInTest();
        List<Posts> posts = LongStream.range(0, 3).mapToObj(i ->
                Posts.builder()
                        .user(userInTest)
                        .title("testPosts" + i)
                        .content("testContent" + i)
                        .postsCategory(PostsCategory.MEDIA)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()
        ).toList();

        postsRepository.saveAll(posts);

        List<Posts> devPosts = LongStream.range(0, 5).mapToObj(i ->
                Posts.builder()
                        .user(userInTest)
                        .title("testPosts" + i)
                        .content("testContent" + i)
                        .postsCategory(PostsCategory.DEV)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()
        ).toList();

        postsRepository.saveAll(devPosts);

        List<Posts> lifePosts = LongStream.range(0, 2).mapToObj(i ->
                Posts.builder()
                        .user(userInTest)
                        .title("testPosts" + i)
                        .content("testContent" + i)
                        .postsCategory(PostsCategory.LIFE)
                        .regDate(LocalDateTime.now())
                        .modDate(LocalDateTime.now())
                        .build()
        ).toList();

        postsRepository.saveAll(devPosts);
        PostSearchForRepository requestDTO = PostSearchForRepository.builder()
                .page(1)
                .size(10)
                .offset(1)
                .category(null)
                .build();
        // when
        List<Posts> postsByCategorySearch = postsRepository.getList(requestDTO);

        //then
        assertThat(postsByCategorySearch.size()).isEqualTo(7);
    }
}