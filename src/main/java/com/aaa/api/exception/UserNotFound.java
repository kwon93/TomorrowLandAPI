package com.aaa.api.exception;

public class UserNotFound extends AAAException{

    private final static String MESSAGE = "찾을 수 없는 회원입니다.";
    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
