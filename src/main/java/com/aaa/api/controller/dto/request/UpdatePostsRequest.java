package com.aaa.api.controller.dto.request;

import com.aaa.api.domain.enumType.PostsCategory;
import com.aaa.api.service.dto.request.UpdatePostsServiceRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePostsRequest {

    private String title;
    private String content;
    private PostsCategory category;

    @Builder
    public UpdatePostsRequest(final String title, final String content, final PostsCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public UpdatePostsServiceRequest toServiceDto(final Long postsId){
        return UpdatePostsServiceRequest.builder()
                .postsId(postsId)
                .title(this.title)
                .content(this.content)
                .build();

    }
}
