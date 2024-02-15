package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.IsRewarded;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class CommentsResponse {

    private long id;
    private String content;
    private String userName;
    private String userEmail;
    private long userId;
    private IsRewarded isRewarded;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public CommentsResponse(final Comment entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.regDate = entity.getRegDate();
        this.modDate = entity.getModDate();
        this.isRewarded = entity.getIsRewarded();
        this.userName = entity.getUsername();
        this.userId = entity.getUsersId();
        this.userEmail = entity.getUserEmail();
    }

}
