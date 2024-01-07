package com.aaa.api.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResult<T> {
    private List<T> commentResponse;

    public CommentResult(List<T> commentResponse) {
        this.commentResponse = commentResponse;
    }
}
