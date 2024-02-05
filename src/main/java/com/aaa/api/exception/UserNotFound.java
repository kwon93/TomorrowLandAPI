package com.aaa.api.exception;

public class UserNotFound extends TomorrowException {

    private final static String MESSAGE = "DB에서 찾을 수 없는 사용자 정보";
    private final static String USER_MESSAGE = "찾을 수 없는 회원입니다.";

    public UserNotFound() {
        super(MESSAGE,USER_MESSAGE);
    }
    public UserNotFound(String message, Throwable cause) {
        super(message,USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
