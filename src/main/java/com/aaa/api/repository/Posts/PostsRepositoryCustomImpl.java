package com.aaa.api.repository.Posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.QPosts;
import com.aaa.api.repository.Posts.dto.PostSearchForRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
}
