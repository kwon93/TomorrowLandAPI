package com.aaa.api.repository.posts;

import com.aaa.api.domain.Posts;
import com.aaa.api.domain.QPosts;
import com.aaa.api.domain.QUsers;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.repository.posts.dto.PostSearchForRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                .leftJoin(QPosts.posts.user, QUsers.users).fetchJoin()
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .where(eqCategory(postSearch.getCategory()), eqKeyword(postSearch.getSearchKeyword()))
                .orderBy(QPosts.posts.id.desc())
                .fetch();
    }

    @Override
    public List<Posts> getNoPagingLists(PostsCategory category) {
        return jpaQueryFactory.selectFrom(QPosts.posts)
                .where(eqCategory(category))
                .fetch();
    }

    private BooleanExpression eqCategory(PostsCategory category){
        if (category == null){
            return null;
        }
        return QPosts.posts.category.eq(category);
    }

    private BooleanExpression eqKeyword(String searchKeyword){
        if (searchKeyword == null){
            return null;
        }
        return QPosts.posts.title.contains(searchKeyword);
    }


    @Override
    public Optional<Posts> getOneByPessimistLock(Long postsId) {
        Posts posts = jpaQueryFactory.selectFrom(QPosts.posts)
                .leftJoin(QPosts.posts.user, QUsers.users).fetchJoin()
                .where(QPosts.posts.id.eq(postsId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
        return Optional.ofNullable(posts);
    }
}
