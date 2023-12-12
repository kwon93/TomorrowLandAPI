package com.aaa.api.domain;

import com.aaa.api.domain.enumType.PostStatus;
import com.aaa.api.dto.request.CreatePostsRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Posts extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  title;

    @Column(nullable = false)
    private String  content;

    private int viewCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated
    private PostStatus postStatus;


    @Builder
    public Posts(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public static Posts of(CreatePostsRequest request){
        return Posts.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
