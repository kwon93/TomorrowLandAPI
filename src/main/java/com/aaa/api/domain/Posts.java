package com.aaa.api.domain;

import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.controller.dto.request.CreatePostsRequest;
import com.aaa.api.controller.dto.request.UpdatePostsRequest;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

    private String imagePath;

    @Enumerated
    private PostsCategory category;

    @Builder
    public Posts(Long id, Users user, String title, String content, String imagePath, PostsCategory postsCategory) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.category = postsCategory;
    }
    public Long getUserId(){
        return this.user.getId();
    }

    public void increaseViewCount(){
        this.viewCount++;
    }


}
