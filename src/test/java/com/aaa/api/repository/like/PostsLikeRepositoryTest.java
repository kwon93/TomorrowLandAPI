package com.aaa.api.repository.like;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class PostsLikeRepositoryTest extends IntegrationTestSupport {


    @Test
    @DisplayName("deleteLikeByUserAndPosts(): 사용자와 게시물을 참조해 좋아요를 삭제해야한다.")
    void test1() {
        //given
        Users users = createUserInTest();
        Posts posts = createPostInTest(users);
        PostsLike like = PostsLike.builder()
                .posts(posts)
                .user(users)
                .build();
        PostsLike save = likeRepository.save(like);

        // when
        likeRepository.deleteLikeByUserAndPosts(users, posts);
        //then
        List<PostsLike> likes = likeRepository.findAll();
        Assertions.assertThat(likes).isEmpty();
    }

}