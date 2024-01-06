package com.aaa.api.service.dto.request;

import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

@Getter
public class CreateUsersServiceRequest {

    private String email;
    private String password;
    private String name;
    private Role role;

    @Builder
    public CreateUsersServiceRequest(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Users toEntity(String encodedPassword){
        return Users.builder()
                .email(this.email)
                .password(encodedPassword)
                .name(this.name)
                .role(this.role)
                .build();
    }
}
