package com.aaa.api.domain;

import com.aaa.api.dto.request.CreateCommentRequest;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.UpdateCommentRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = {@Index(name = "IDX_COMMENT_POST_ID", columnList = "posts_id")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public Comment(Long id, String username, String password, String content, Users user, Posts posts) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.content = content;
        this.user = user;
        this.posts = posts;
    }

    public static Comment of(Posts post, CreateCommentRequest request, String encodedPW){
        return Comment.builder()
                .posts(post)
                .content(request.getContent())
                .password(encodedPW)
                .username(request.getName())
                .build();
    }

    public Comment updateComment(UpdateCommentRequest request){
        this.content = request.getContent();

        return this;
    }
}
