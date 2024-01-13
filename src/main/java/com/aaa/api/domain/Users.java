package com.aaa.api.domain;

import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.exception.NotEnoughPoint;
import jakarta.persistence.*;
import lombok.*;

import static com.aaa.api.domain.enumType.UserLevel.*;

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
        this.point = point == null ? 200 : point;
        this.userLevel = Beginner;
        this.roles = role;
    }

    public void decreasePoint() {
        if (this.point >= 20){
            this.point -= 20;
            this.userLevel = measurementLevel(this.point);
        }else {
            throw new NotEnoughPoint();
        }
    }

    public void increasePoint(){
        this.point += 50;
        this.userLevel = measurementLevel(this.point);
    }
}
