package com.aaa.api.service;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.domain.Comment;
import com.aaa.api.domain.Posts;
import com.aaa.api.domain.Users;
import com.aaa.api.domain.enumType.IsRewarded;
import com.aaa.api.domain.enumType.Role;
import com.aaa.api.domain.enumType.UserLevel;
import com.aaa.api.exception.DuplicateEmail;
import com.aaa.api.exception.UserNotFound;
import com.aaa.api.service.dto.request.CreateUsersServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UsersServiceTest extends IntegrationTestSupport {

    @Test
    @DisplayName("createUser(): 요청에따른 회원가입에 성공해야한다.")
    void test1() throws Exception {
        //given
        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersServiceRequest request = CreateUsersServiceRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.ADMIN)
                .build();

        // when
        usersService.createUser(request);

        //then
        List<Users> users = usersRepository.findAll();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getEmail()).isEqualTo(email);
        assertTrue(passwordEncoder.matches(password, users.get(0).getPassword()));
        assertThat(users.get(0).getName()).isEqualTo(name);
        assertThat(users.get(0).getUserLevel()).isEqualTo(UserLevel.Beginner);
    }


    @Test
    @DisplayName("createUser(): 요청에 중복 이메일 존재시 DuplicateEmailException이 발생해야한다.")
    void test2() throws Exception {
        //given
        createUserInTest();

        final String email = "kwon93@naver.com";
        final String password = "kdh1234";
        final String name = "kwon";

        CreateUsersServiceRequest request = CreateUsersServiceRequest.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();

        // when then
        DuplicateEmail e = assertThrows(DuplicateEmail.class, () -> {
            usersService.createUser(request);
        });

        assertThat(e.getMessage()).isEqualTo("DB에 이미 중복되는 이메일이 존재");
    }



    @Test
    @Transactional
    @DisplayName("reward(): 질문자와 답변자의 점수 주고받기에 성공해야한다.")
    void test3() {
        //given
        Users questionUser = createUserInTest(220);
        Users answerUser = createUserInTest(151);
        Posts postInTest = createPostInTest();
        Comment commentInTest = Comment.builder()
                .posts(postInTest)
                .username(answerUser.getName())
                .password("123456")
                .isRewarded(IsRewarded.False)
                .build();
        Comment comment = commentRepository.save(commentInTest);
        // when
        usersService.reward(questionUser.getId(), answerUser.getId(), comment.getId());

        //then
        Users decreasedUser = usersRepository.findById(questionUser.getId())
                .orElseThrow(UserNotFound::new);
        Users increasedUser = usersRepository.findById(answerUser.getId())
                .orElseThrow(UserNotFound::new);

        assertThat(decreasedUser)
                .extracting("point","userLevel")
                .containsExactlyInAnyOrder(200,UserLevel.Beginner);
        assertThat(increasedUser)
                .extracting("point","userLevel")
                .containsExactlyInAnyOrder(201,UserLevel.Intermediate);
        assertThat(comment.getIsRewarded()).isEqualTo(IsRewarded.True);
    }


}