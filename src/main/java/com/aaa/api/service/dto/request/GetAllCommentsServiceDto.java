package com.aaa.api.service.dto.request;

import com.aaa.api.config.security.CustomUserPrincipal;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GetAllCommentsServiceDto {

    private final Long postsId;
    private final Long userId;

    @Builder
    public GetAllCommentsServiceDto(final Long postsId, final Long userId) {
            this.postsId = postsId;
            this.userId = userId;
    }
}
