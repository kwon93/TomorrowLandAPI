package com.aaa.api.domain;

import com.aaa.api.IntegrationTestSupport;
import com.aaa.api.exception.NotEnoughPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsersTest extends IntegrationTestSupport {



    @Test
    @DisplayName("decreasePoint(): 사용자의 점수를 20점에서 20점 차감하는데에 성공한다.")
    void test1() {
        //given
        Users userInTest = createUserInTest(20);
        // when
        userInTest.decreasePoint();
        //then
        assertThat(userInTest.getPoint()).isZero();
    }

    @Test
    @DisplayName("decreasePoint(): 사용자의 점수가 19점일때 20점 차감시 NotEnoughPointException이 발생한다.")
    void test2() {
        //given
        Users userInTest = createUserInTest(19);
        // when then
        assertThatThrownBy(userInTest::decreasePoint)
                .isInstanceOf(NotEnoughPoint.class)
                .hasMessage("점수가 부족합니다!");
    }


    @Test
    @DisplayName("increasePoint(): 사용자의 점수를 200점에서 50점 증가하는데에 성공한다.")
    void test3() {
        //given
        Users userInTest = createUserInTest();
        // when
        userInTest.increasePoint();
        //then
        assertThat(userInTest.getPoint()).isEqualTo(250);
    }



}