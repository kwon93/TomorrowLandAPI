package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.exception.DuplicateLike;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.posts.PostsRepository;
import com.aaa.api.repository.like.PostsLikeRepository;
import com.aaa.api.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsLikeService {

    private final PostsLikeRepository likeRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void increase(final long postsId, final long userId) {
        final Posts posts = postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);
        final Users users = usersRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        if (likeRepository.findByUserAndPosts(users, posts).isPresent()){
            throw new DuplicateLike();
        }

        posts.increaseLikeCount();

        final PostsLike like = PostsLike.builder()
                .user(users)
                .posts(posts)
                .build();

        likeRepository.save(like);
    }

    @Transactional
    public void decrease(final Long postsId, final Long userId) {
        final Posts posts = postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);
        final Users users = usersRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        posts.decreaseLikeCount();
    }
}
