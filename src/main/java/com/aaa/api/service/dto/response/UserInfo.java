package com.aaa.api.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import com.aaa.api.domain.Users;

@Getter
public class UserInfo {

    private final String email;
    private final String name;
    private final Integer userPoint;
    private final Integer userAnswer;

    @Builder
    public UserInfo(Users entity, Integer userAnswer) {
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.userPoint = entity.getPoint();
        this.userAnswer = userAnswer;
    }


}
