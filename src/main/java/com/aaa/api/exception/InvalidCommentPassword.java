package com.aaa.api.exception;

public class InvalidCommentPassword extends AAAException {

    private static final String MESSAGE = "잘못된 비밀번호 입니다.";

    public InvalidCommentPassword() {
        super(MESSAGE);
    }

    public InvalidCommentPassword(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
