package com.aaa.api.domain.enumType;

import com.aaa.api.exception.InvalidUserLevel;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserLevel {

    Beginner(0, 200),
    Intermediate(201 , 400 ),
    Advanced(401 , Integer.MAX_VALUE);

    private final int minPoint;
    private final int maxPoint;
    UserLevel(int minPoint, int maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }
    public static UserLevel measurementLevel(Integer userPoint){
        return Arrays.stream(UserLevel.values())
                .filter(userLevel -> userPoint >= userLevel.getMinPoint() && userPoint <= userLevel.getMaxPoint())
                .findFirst()
                .orElseThrow(InvalidUserLevel::new);
    }



}
