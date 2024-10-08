package com.aaa.api.domain;

import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.exception.NegativeScoreException;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(indexes = {@Index(name = "idx_post_title", columnList = "title")} )
public class Posts extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  title;

    @Column(nullable = false)
    private String  content;

    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private PostsCategory category;

    private int likeCount;

    @Builder
    public Posts(Long id, Users user, String title, String content,
                 String imagePath, PostsCategory postsCategory, int viewCount,
                 int likeCount, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.category = postsCategory;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.regDate = regDate;
        this.modDate = modDate;
    }
    public Long getUserId(){
        return this.user.getId();
    }
    public void increaseViewCount(){
        this.viewCount++;
    }
    public void increaseLikeCount(){this.likeCount++;}
    public void decreaseLikeCount(){
        if (this.likeCount > 0){
            this.likeCount--;
        }else {
            throw new NegativeScoreException();
        }
    }
    public String getUserName(){
        return this.user.getName();
    }
    public String getUserEmail(){
        return this.user.getEmail();
    }
    public void update(final UpdatePostsServiceRequest updateRequest) {
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
        this.category = updateRequest.getCategory();
    }
}
