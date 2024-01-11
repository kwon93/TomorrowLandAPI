package com.aaa.api.service.dto.request;

import com.aaa.api.config.security.CustomUserPrincipal;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class LikeServiceRequest {

    private final long postsId;
    private final long userId;

    @Builder
    public LikeServiceRequest(long postsId, CustomUserPrincipal user) {
        this.postsId = postsId;
        this.userId = user.getUserId();
    }
}
