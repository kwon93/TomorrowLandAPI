package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.Posts.PostsRepository;
import com.aaa.api.repository.PostsLikeRepository;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsLikeService {

    private final PostsLikeRepository postsLikeRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void increase(long postsId, long userId) {
        Posts posts = postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);
        Users users = usersRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
        posts.increaseLikeCount();

        PostsLike like = PostsLike.builder()
                .user(users)
                .posts(posts)
                .build();

        postsLikeRepository.save(like);
    }
}
