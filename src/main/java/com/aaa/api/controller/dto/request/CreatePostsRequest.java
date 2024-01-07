package com.aaa.api.controller.dto.request;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostsRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private PostsCategory category = PostsCategory.DEV;

    @Builder
    public CreatePostsRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public CreatePostsServiceRequest toServiceDto(final CustomUserPrincipal userPrincipal){
        return CreatePostsServiceRequest.builder()
                .userId(userPrincipal.getUserId())
                .title(this.title)
                .content(this.content)
                .category(this.category)
                .build();
    }
}
