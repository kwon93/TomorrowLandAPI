package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.DuplicateLike;
import com.aaa.api.exception.NegativeScoreException;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
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
        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);
        // when
        likeService.likeIncreaseProcess(postInTest.getId(), userInTest.getId());

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
        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);
        // when
        likeService.likeIncreaseProcess(postInTest.getId(), userInTest.getId());

        //then
        Posts findPosts = postsRepository.findById(postInTest.getId()).orElseThrow(PostNotfound::new);
        assertThat(findPosts.getLikeCount()).isOne();
    }


    @Test
    @DisplayName("increase(): 잘못된 사용자인 경우 UserNotFound Exception을 반환한다.")
    void test3() {
        //given
        final long invalidUserId = 999L;
        Posts postInTest = createPostInTest(createUserInTest());
        // when
        assertThatThrownBy(()-> likeService.likeIncreaseProcess(postInTest.getId() ,invalidUserId))
                .isInstanceOf(UserNotFound.class)
                .hasMessage("DB에서 찾을 수 없는 사용자 정보");
    }


    @Test
    @DisplayName("decrease(): 좋아요 취소 요청으로 Posts likeCount가 0이여야한다.")
    void test4() {
        //given
        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);
        // when
        likeService.likeIncreaseProcess(postInTest.getId(), userInTest.getId());
        likeService.decrease(postInTest.getId(), userInTest.getId());
        //then
        Posts posts = postsRepository.findById(postInTest.getId()).orElseThrow(PostNotfound::new);
        assertThat(posts.getLikeCount()).isZero();
    }

    @Test
    @Transactional
    @DisplayName("increase(): 이미 좋아요한 게시물일 경우 DuplicateLikeException을 반환한다.")
    void test5() {
        //given
        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);
        // when
        likeService.likeIncreaseProcess(postInTest.getId(), userInTest.getId());

        assertThatThrownBy(()-> likeService.likeIncreaseProcess(postInTest.getId(), userInTest.getId()))
                .isInstanceOf(DuplicateLike.class)
                .hasMessage("사용자의 중복 추천 게시물");
    }

    @Test
    @DisplayName("decrease(): 좋아요는 0이하로 내려 갈 수 없다.")
    void test6() {
        //given
        Users userInTest = createUserInTest();
        Posts postInTest = createPostInTest(userInTest);
        // when then
        assertThatThrownBy(()-> likeService.decrease(postInTest.getId(), userInTest.getId()))
                .isInstanceOf(NegativeScoreException.class)
                .hasMessage("최소 추천수 0 미만 오류");

    }

}