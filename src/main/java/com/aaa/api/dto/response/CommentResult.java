package com.aaa.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResult<T> {
    private List<T> commentResponse;

    public CommentResult(List<T> commentResponse) {
        this.commentResponse = commentResponse;
    }
}
