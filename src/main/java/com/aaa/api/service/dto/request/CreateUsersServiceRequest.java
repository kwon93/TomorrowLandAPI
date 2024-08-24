package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateUsersServiceRequest {

    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    @Builder
    private CreateUsersServiceRequest(final String email, final String password, final String name, final Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Users toEntity(final String encodedPassword){
        return Users.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .role(this.role)
                .build();
    }
}
