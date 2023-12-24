package com.aaa.api.domain;

import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.dto.request.CreateUsersRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Users extends BaseEntity implements UserDetails {

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Builder
    public Users(String email, String password, String name, Integer point, UserLevel userLevel, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.point = 100;
        this.userLevel = UserLevel.Beginner;
        this.roles.add(role.toString());
    }

    public static Users of(CreateUsersRequest request, String password){
        return Users.builder()
                .email(request.getEmail())
                .password(password)
                .name(request.getName())
                .build();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
