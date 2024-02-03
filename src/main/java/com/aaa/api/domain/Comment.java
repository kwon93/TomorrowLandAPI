package com.aaa.api.domain;

import com.aaa.api.domain.enumType.IsRewarded;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "posts_id")
    private Posts posts;
    @ManyToOne(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "users_id")
    private Users users;
    private IsRewarded isRewarded;

    @Builder
    public Comment(Long id, Users users, String content, Posts posts,
                   LocalDateTime regDate, LocalDateTime modDate, IsRewarded isRewarded) {
        this.id = id;
        this.content = content;
        this.posts = posts;
        this.isRewarded = isRewarded == null ? IsRewarded.False : isRewarded;
        this.regDate = regDate;
        this.modDate = modDate;
        this.users = users;
    }

    public void updateRewardState(){
        this.isRewarded = IsRewarded.True;
    }

    public Long getUsersId(){
        return this.users.getId();
    }
}
