package com.aaa.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentRequest {


    @NotBlank
    @Size(max = 500, message = "답변은 500자 이하로 작성해주세요.")
    private String content;


    @Builder
    public UpdateCommentRequest(String content) {
        this.content = content;
    }
}
