package com.aaa.api.dto.request;

import com.aaa.api.domain.enumType.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@NoArgsConstructor
public class CreatePostsRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private PostStatus postStatus = PostStatus.PUBLIC;

    @Builder
    public CreatePostsRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
