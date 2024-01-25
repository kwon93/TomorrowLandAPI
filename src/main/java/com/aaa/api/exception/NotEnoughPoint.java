package com.aaa.api.exception;

public class NotEnoughPoint extends TomorrowException {

    private static final String  MESSAGE = "점수가 부족합니다!";

    public NotEnoughPoint() {
        super(MESSAGE);
    }

    public NotEnoughPoint(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
