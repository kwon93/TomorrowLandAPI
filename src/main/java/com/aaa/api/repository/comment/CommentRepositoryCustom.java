package com.aaa.api.repository.comment;

import com.aaa.api.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getCommentListByQueryDSL(long postsId);
}
