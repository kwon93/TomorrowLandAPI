package com.aaa.api.exception;

public class InvalidCommentPassword extends TomorrowException {

    private static final String MESSAGE = "비밀번호 입력 실패";
    private static final String USER_MESSAGE = "잘못된 이메일 또는 비밀번호 입니다.";

    public InvalidCommentPassword() {
        super(MESSAGE,USER_MESSAGE);
    }

    public InvalidCommentPassword(String message, Throwable cause) {
        super(message, USER_MESSAGE,cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
