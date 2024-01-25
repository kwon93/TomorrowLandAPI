package com.aaa.api.exception;

public class Unauthorized extends TomorrowException {

    private final static String MESSAGE = "인증 실패";
    private final static String USER_MESSAGE = "죄송합니다. 인증에 실패했습니다.";

    public Unauthorized() {
        super(MESSAGE,USER_MESSAGE);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
