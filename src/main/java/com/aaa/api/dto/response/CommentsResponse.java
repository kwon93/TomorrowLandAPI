package com.aaa.api.dto.response;

import com.aaa.api.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentsResponse {

    private String content;
    private String username;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public CommentsResponse(Comment entity) {
        this.content = entity.getContent();
        this.username = entity.getUsername();
        this.regDate = entity.getRegDate();
        this.modDate = entity.getModDate();
    }
}
