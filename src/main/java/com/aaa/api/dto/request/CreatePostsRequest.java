package com.aaa.api.dto.request;

import com.aaa.api.domain.enumType.PostsCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostsRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private PostsCategory category = PostsCategory.DEV;

    @Builder
    public CreatePostsRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
