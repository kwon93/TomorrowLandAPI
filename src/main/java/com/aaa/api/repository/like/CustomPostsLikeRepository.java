package com.aaa.api.repository.like;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomPostsLikeRepository {

    Long deleteLikeByUserAndPosts(Users user, Posts posts);

}
