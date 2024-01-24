package com.aaa.api.domain.enumType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

class UserLevelTest {


    @Test
    @DisplayName("meditateLevel():점수가 0점일 경우 Beginner 등급을 반환한다.")
    void test1() {
        //given
        final Integer testPoint = 0;

        // when
        UserLevel userLevel = UserLevel.measurementLevel(testPoint);

        //then
        Assertions.assertThat(userLevel).isEqualTo(UserLevel.Beginner);
    }

    @Test
    @DisplayName("meditateLevel(): 점수가 200점일 경우 Beginner 등급을 반환한다.")
    void test2() {
        //given
        final Integer testPoint = 200;

        // when
        UserLevel userLevel = UserLevel.measurementLevel(testPoint);

        //then
        Assertions.assertThat(userLevel).isEqualTo(UserLevel.Beginner);
    }

    @Test
    @DisplayName("meditateLevel(): 점수가 201점일 경우 Intermediate 등급을 반환한다.")
    void test3() {
        //given
        final Integer testPoint = 201;

        // when
        UserLevel userLevel = UserLevel.measurementLevel(testPoint);

        //then
        Assertions.assertThat(userLevel).isEqualTo(UserLevel.Intermediate);
    }

    @Test
    @DisplayName("meditateLevel(): 점수가 400점일 경우 Intermediate 등급을 반환한다.")
    void test4() {
        //given
        final Integer testPoint = 400;

        // when
        UserLevel userLevel = UserLevel.measurementLevel(testPoint);

        //then
        Assertions.assertThat(userLevel).isEqualTo(UserLevel.Intermediate);
    }

    @Test
    @DisplayName("meditateLevel(): 점수가 401점 이상일 경우 Intermediate 등급을 반환한다.")
    void test5() {
        //given
        final int minPoint = 401;
        int maxPoint = Integer.MAX_VALUE;

        Random random = new Random();
        int testPoint = random.nextInt((maxPoint - minPoint) + 1) + minPoint;

        // when
        UserLevel userLevel = UserLevel.measurementLevel(testPoint);

        //then
        Assertions.assertThat(userLevel).isEqualTo(UserLevel.Advanced);
    }

}