package com.aaa.api.exception;

public class InvalidSignInInfomation extends TomorrowException {

    private final static String MESSAGE = "이메일 및 비밀번호가 일치하지않습니다.";

    public InvalidSignInInfomation() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
