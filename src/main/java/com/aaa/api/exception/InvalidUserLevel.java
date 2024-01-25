package com.aaa.api.exception;

public class InvalidUserLevel extends TomorrowException {

    private static final String MESSAGE = "사용자의 등급 연산에 문제가 발생했습니다.";

    public InvalidUserLevel() {
        super(MESSAGE);
    }

    public InvalidUserLevel(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 500;
    }
}
