package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.request.UpdateCommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCommentRequest {


    @NotBlank
    @Size(max = 500, message = "답변은 500자 이하로 작성해주세요.")
    private String content;


    @Builder
    public UpdateCommentRequest(final String content) {
        this.content = content;
    }

    public UpdateCommentServiceRequest toServiceDto(final Long commentId){
        return UpdateCommentServiceRequest.builder()
                .content(this.content)
                .commentId(commentId)
                .build();
    }
}
