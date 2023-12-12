package com.aaa.api.repository;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.QPosts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Posts> getList(int page) {
        return jpaQueryFactory.selectFrom(QPosts.posts)
                .limit(10)
                .offset(((long)( page - 1) * 10 ))
                .fetch();
    }
}


