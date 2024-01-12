package com.aaa.api.domain;

import com.aaa.api.domain.enumType.PostsCategory;
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

    private int likeCount;

    @Builder
    public Posts(Long id, Users user, String title, String content, String imagePath, PostsCategory postsCategory, int likeCount) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.category = postsCategory;
        this.likeCount = likeCount;
    }
    public Long getUserId(){
        return this.user.getId();
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void increaseLikeCount(){this.likeCount++;}

    public void decreaseLikeCount(){
        this.likeCount--;
    }


}
