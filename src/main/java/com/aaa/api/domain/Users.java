package com.aaa.api.domain;

import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.controller.dto.request.CreateUsersRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;

    @Enumerated(EnumType.STRING)
    private Role roles;




    @Builder
    public Users(long id,String email, String password, String name, Integer point, UserLevel userLevel, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.point = 100;
        this.userLevel = UserLevel.Beginner;
        this.roles = role;
    }

    public static Users of(CreateUsersRequest request, String password){
        return Users.builder()
                .email(request.getEmail())
                .password(password)
                .name(request.getName())
                .role(request.getRole())
                .point(100)
                .userLevel(UserLevel.Beginner)
                .build();
    }


}
