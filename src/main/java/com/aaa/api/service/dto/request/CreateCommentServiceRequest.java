package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCommentServiceRequest {

    private final String content;
    private final Long postsId;
    private final Long usersId;

    @Builder
    public CreateCommentServiceRequest(final String content, final Long postsId, Long usersId) {
        this.content = content;
        this.postsId = postsId;
        this.usersId = usersId;
    }

    public Comment toEntity(final Posts posts, final Users users){
        return Comment.builder()
                .posts(posts)
                .users(users)
                .content(this.content)
                .build();
    }

}
