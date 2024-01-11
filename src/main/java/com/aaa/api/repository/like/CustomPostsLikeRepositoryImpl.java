package com.aaa.api.repository.like;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.aaa.api.domain.QPostsLike.*;

@RequiredArgsConstructor
public class CustomPostsLikeRepositoryImpl implements CustomPostsLikeRepository{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Long deleteLikeByUserAndPosts(Users user, Posts posts) {
        return jpaQueryFactory.delete(postsLike)
                .where(postsLike.posts.eq(posts).and(postsLike.user.eq(user)))
                .execute();
    }
}
