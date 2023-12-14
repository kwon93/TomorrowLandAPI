package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.dto.request.CreateUsersRequest;
import com.aaa.api.exception.DuplicateEmail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UsersServiceTest extends IntegrationTestSupport {



    @Test
    @DisplayName("createUser(): 요청에따른 회원가입에 성공해야한다.")
    void test1() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersRequest request = CreateUsersRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        // when
        usersService.createUser(request);

        //then
        List<Users> users = usersRepository.findAll();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getEmail()).isEqualTo(email);
        assertThat(users.get(0).getPassword()).isEqualTo(password);
        assertThat(users.get(0).getName()).isEqualTo(name);
        assertThat(users.get(0).getPoint()).isEqualTo(100);
        assertThat(users.get(0).getUserLevel()).isEqualTo(UserLevel.Beginner);
        assertThat(users.get(0).getRole()).isEqualTo(Role.ROLE_USER);
    }


    @Test
    @DisplayName("createUser(): 요청에 중복 이메일 존재시 DuplicateEmailException이 발생해야한다.")
    void test2() throws Exception {
        //given
        createUserInTest();

        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersRequest request = CreateUsersRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        // when then
        DuplicateEmail e = assertThrows(DuplicateEmail.class, () -> {
            usersService.createUser(request);
        });

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일 입니다.");


    }
}