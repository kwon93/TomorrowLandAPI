package com.aaa.api.repository.like;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsLikeRepository extends JpaRepository<PostsLike, Long>, CustomPostsLikeRepository{
    boolean existsByUserAndPosts(Users users, Posts posts);
}
