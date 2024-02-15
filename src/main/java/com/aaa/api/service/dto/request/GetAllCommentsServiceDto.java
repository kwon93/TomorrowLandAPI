package com.aaa.api.service.dto.request;

import com.aaa.api.config.security.CustomUserPrincipal;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GetAllCommentsServiceDto {

    private final Long postsId;

    @Builder
    public GetAllCommentsServiceDto(final Long postsId) {
            this.postsId = postsId;
    }
}
