package com.aaa.api.domain;

import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.dto.request.CreatePostsRequest;
import com.aaa.api.dto.request.UpdatePostsRequest;
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
    private PostsCategory category;


    @Builder
    public Posts(String title, String content, PostsCategory postsCategory) {
        this.title = title;
        this.content = content;
        this.category = postsCategory;
    }

    public Posts updatePosts(UpdatePostsRequest request){
        return Posts.builder()
                .title(request.getTitle())
                .content(request.getCotent())
                .postsCategory(request.getPostsCategory())
                .build();
    }


    public static Posts of(CreatePostsRequest request){
        return Posts.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
