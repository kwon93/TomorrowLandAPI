package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCommentRequest {

    @NotBlank
    @Size(max = 500, message = "답변은 500자 이하로 작성해주세요.")
    private String content;

    @Builder
    public CreateCommentRequest(final String content) {
        this.content = content;
    }

    public CreateCommentServiceRequest toServiceDto(final Long postsId, final Long userId){
        return CreateCommentServiceRequest.builder()
                .content(this.content)
                .postsId(postsId)
                .usersId(userId)
                .build();
    }

}
