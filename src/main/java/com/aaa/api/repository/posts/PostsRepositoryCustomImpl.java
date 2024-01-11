package com.aaa.api.repository.posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.QPosts;
import com.aaa.api.repository.posts.dto.PostSearchForRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostsRepositoryCustomImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Posts> getList(final PostSearchForRepository postSearch) {
        return jpaQueryFactory.selectFrom(QPosts.posts)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPosts.posts.id.desc())
                .fetch();
    }

    @Override
    public Optional<Posts> getOneByPessimistLock(Long postsId) {
        Posts posts = jpaQueryFactory.selectFrom(QPosts.posts)
                .where(QPosts.posts.id.eq(postsId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
        return Optional.ofNullable(posts);
    }
}
