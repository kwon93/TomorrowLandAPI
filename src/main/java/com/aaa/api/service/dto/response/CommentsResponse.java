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
    private IsRewarded isRewarded;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    @Setter
    private boolean modifiable;

    public CommentsResponse(final Comment entity, final boolean modifiable) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.regDate = entity.getRegDate();
        this.modDate = entity.getModDate();
        this.isRewarded = entity.getIsRewarded();
        this.userName = entity.getUsers().getName();
        this.modifiable = modifiable;
    }

}
