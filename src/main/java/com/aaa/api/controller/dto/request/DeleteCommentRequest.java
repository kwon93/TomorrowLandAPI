package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.request.DeleteCommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteCommentRequest {

    @NotBlank
    @Size(min = 4, max = 12, message = "비밀번호는 4글자 이상 12글자 이하로 입력해주세요.")
    private String password;

    @Builder
    public DeleteCommentRequest(String password) {
        this.password = password;
    }

    public DeleteCommentServiceRequest toServiceDto(Long commentId){
        return DeleteCommentServiceRequest.builder()
                .password(this.password)
                .commentId(commentId)
                .build();
    }
}
