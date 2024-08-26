package com.aaa.api.service;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.PostsLike;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.DuplicateLike;
import com.aaa.api.exception.PostNotfound;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.repository.UsersRepository;
import com.aaa.api.repository.like.PostsLikeRepository;
import com.aaa.api.repository.posts.PostsRepository;
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
    public void likeIncreaseProcess(final long postsId, final long userId) {
        final Posts post = pessimisticFindPosts(postsId);
        final Users user = findUserBy(userId);
        duplicateLikeValidationBy(user, post);
        post.increaseLikeCount();
        persistPostsLike(user, post);
    }

    @Transactional
    public void decrease(final Long postsId, final Long userId) {
        Posts decreaseTargetPosts = pessimisticFindPosts(postsId);
        invalidUserValidation(userId);
        decreaseTargetPosts.decreaseLikeCount();
    }

    private void persistPostsLike(Users user, Posts post) {
        final PostsLike like = PostsLike.builder()
                .user(user)
                .posts(post)
                .build();
        likeRepository.save(like);
    }

    private void duplicateLikeValidationBy(Users user, Posts post) {
        if (likeRepository.existsByUserAndPosts(user, post)) {
            throw new DuplicateLike();
        }
    }

    private Posts pessimisticFindPosts(long postsId) {
        return postsRepository.getOneByPessimistLock(postsId)
                .orElseThrow(PostNotfound::new);
    }

    private Users findUserBy(long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
    }

    private void invalidUserValidation(Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            throw new UserNotFound();
        }
    }
}
