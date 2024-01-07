package com.aaa.api.repository.comment;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getCommentListByQueryDSL(final long postsId) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.posts.id.eq(postsId))
                .orderBy(QComment.comment.regDate.asc())
                .fetch();
    }
}

