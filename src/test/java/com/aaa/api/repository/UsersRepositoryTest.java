package com.aaa.api.repository;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Users;
import com.aaa.api.exception.InvalidSignInInfomation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;




class UsersRepositoryTest extends IntegrationTestSupport {


    @Test
    @DisplayName("findByEmail(): 이메일과 일치하는 사용자를 DB에서 반환한다.")
    void test1() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        Users userInTest = createUserInTest();

        // when
        Users users = usersRepository.findByEmail(email).get();

        //then
        assertThat(users).isNotNull();
        assertThat(users.getEmail()).isEqualTo(email);

    }



    @Test
    @DisplayName("findByEmailAndPassword(): 이메일과 비밀번호가 일치하는 사용자를 DB에서 반환한다.")
    void test2() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        final String password = "kdh1234";


        Users userInTest = createUserInTest();
        // when
        Users users = usersRepository.findByEmailAndPassword(email, password)
                .orElseThrow(InvalidSignInInfomation::new);
        //then
        assertThat(users.getEmail()).isEqualTo(email);
        assertThat(users.getPassword()).isEqualTo(password);
    }


}