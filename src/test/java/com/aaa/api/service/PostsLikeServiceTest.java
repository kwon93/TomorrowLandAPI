package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PostsLikeServiceTest extends IntegrationTestSupport {

    @Test
    @Transactional
    @DisplayName("increase(): 좋아요 요청에 성공한 뒤 사용자와 해당 게시글을 반환한다.")
    void test1() {
        //given
        Posts postInTest = createPostInTest();
        Users userInTest = createUserInTest();
        // when
        likeService.increase(postInTest.getId(), userInTest.getId());

        //then
        List<PostsLike> likes = likeRepository.findAll();
        PostsLike postsLike = likes.get(0);

        assertThat(postsLike)
                .extracting("user.id","posts.id")
                .containsExactlyInAnyOrder(postInTest.getId(), userInTest.getId());
    }


    @Test
    @DisplayName("increase(): 좋아요 요청 후 게시물의 likeCount가 증가되있어야한다.")
    void test2() {
        //given
        Posts postInTest = createPostInTest();
        Users userInTest = createUserInTest();
        // when
        likeService.increase(postInTest.getId(), userInTest.getId());

        //then
        Posts findPosts = postsRepository.findById(postInTest.getId()).orElseThrow(PostNotfound::new);
        assertThat(findPosts.getLikeCount()).isOne();
    }


    @Test
    @DisplayName("increase(): 잘못된 사용자인 경우 UserNotFound Exception을 반환한다.")
    void test3() {
        //given
        final long invalidUserId = 999L;
        Posts postInTest = createPostInTest();
        // when
        assertThatThrownBy(()-> likeService.increase(postInTest.getId() ,invalidUserId))
                .isInstanceOf(UserNotFound.class)
                .hasMessage("찾을 수 없는 회원입니다.");
    }

}