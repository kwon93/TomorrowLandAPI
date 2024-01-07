package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCommentServiceRequest {

    private final String username;
    private final String password;
    private final String content;
    private final Long postsId;

    @Builder
    public CreateCommentServiceRequest(final String username, final String password, final String content, final Long postsId) {
        this.username = username;
        this.password = password;
        this.content = content;
        this.postsId = postsId;
    }

    public Comment toEntity(final Posts posts, final String encodedPassword){
        return Comment.builder()
                .posts(posts)
                .username(this.username)
                .password(encodedPassword)
                .content(this.content)
                .build();
    }

}
