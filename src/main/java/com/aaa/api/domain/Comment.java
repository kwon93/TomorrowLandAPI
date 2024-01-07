package com.aaa.api.domain;

import com.aaa.api.controller.dto.request.CreateCommentRequest;
import com.aaa.api.controller.dto.request.UpdateCommentRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public Comment(Long id, String username, String password, String content, Posts posts, LocalDateTime regDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.content = content;
        this.posts = posts;
        this.regDate = regDate;
    }
}
