package com.aaa.api.controller.dto.request;

import com.aaa.api.config.security.CustomUserPrincipal;
import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.CreatePostsServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostsRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "카테고리를 선택해주세요.")
    private String  category;
    private String imagePath;

    @Builder
    public CreatePostsRequest(final String  category,final String title, final String content, String imagePath) {
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.category = category;
    }

    public CreatePostsServiceRequest toServiceDto(final CustomUserPrincipal userPrincipal){
        return CreatePostsServiceRequest.builder()
                .userId(userPrincipal.getUserId())
                .title(this.title)
                .content(this.content)
                .category(enumChecker(this.category))
                .imagePath(imagePath)
                .build();
    }

    public PostsCategory enumChecker(String stringToEnum){
        return Arrays.stream(PostsCategory.values())
                .filter(category1 -> category1.value().equals(stringToEnum))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
