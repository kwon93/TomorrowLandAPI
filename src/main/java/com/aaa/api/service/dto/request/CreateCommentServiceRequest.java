package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCommentServiceRequest {

    private String username;
    private String password;
    private String content;
    private Long postsId;

    @Builder
    public CreateCommentServiceRequest(String username, String password, String content, Long postsId) {
        this.username = username;
        this.password = password;
        this.content = content;
        this.postsId = postsId;
    }

    public Comment toEntity(Posts posts, String encodedPassword){
        return Comment.builder()
                .posts(posts)
                .username(this.username)
                .password(encodedPassword)
                .content(this.content)
                .build();
    }

}
