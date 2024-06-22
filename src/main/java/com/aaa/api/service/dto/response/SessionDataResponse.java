package com.aaa.api.service.dto.response;

import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionDataResponse {

    private Long id;
    private String email;
    private String name;
    private Role role;

    @Builder
    public SessionDataResponse(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static SessionDataResponse of(Users entity) {
        return SessionDataResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .role(entity.getRoles())
                .build();
    }
}
