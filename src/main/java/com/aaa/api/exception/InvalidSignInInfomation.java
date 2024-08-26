package com.aaa.api.exception;

public class InvalidSignInInfomation extends TomorrowException {

    private static final String MESSAGE = "이메일 또는 비밀번호 인증 실패";
    private static final String USER_MESSAGE = "이메일 및 비밀번호가 일치하지않습니다.";

    public InvalidSignInInfomation() {
        super(MESSAGE,USER_MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
