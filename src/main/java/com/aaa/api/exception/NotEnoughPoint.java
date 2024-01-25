package com.aaa.api.exception;

public class NotEnoughPoint extends TomorrowException {

    private static final String  MESSAGE = "사용자의 점수 부족";
    private static final String USER_MESSAGE = "점수가 부족합니다!";

    public NotEnoughPoint() {
        super(MESSAGE, USER_MESSAGE);
    }

    public NotEnoughPoint(Throwable cause) {
        super(MESSAGE,USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
