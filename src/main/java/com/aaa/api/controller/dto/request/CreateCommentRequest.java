package com.aaa.api.controller.dto.request;

import com.aaa.api.service.dto.request.CreateCommentServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    @NotBlank(message = "작성자명을 입력해주세요.")
    @Size(min = 3,max = 10, message = "작성자명은 3글자 이상 10글자 이하로 입력해주세요.")
    private String username;
    @NotBlank
    @Size(min = 4, max = 12, message = "비밀번호는 4글자 이상 12글자 이하로 입력해주세요.")
    private String password;
    @NotBlank
    @Size(max = 500, message = "답변은 500자 이하로 작성해주세요.")
    private String content;


    @Builder
    public CreateCommentRequest(String username, String password, String content) {
        this.username = username;
        this.password = password;
        this.content = content;
    }

    public CreateCommentServiceRequest toServiceDto(Long postsId){
        return CreateCommentServiceRequest.builder()
                .content(this.content)
                .username(this.username)
                .password(this.password)
                .postsId(postsId)
                .build();
    }

}
