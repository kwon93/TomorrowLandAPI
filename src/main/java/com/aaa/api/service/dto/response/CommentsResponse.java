package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentsResponse {

    private String content;
    private String username;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder
    public CommentsResponse(final Comment entity) {
        this.content = entity.getContent();
        this.username = entity.getUsername();
        this.regDate = entity.getRegDate();
        this.modDate = entity.getModDate();
    }

}
