package com.aaa.api.exception;

public class Unauthorized extends AAAException {

    private final static String MESSAGE = "인증에 실패했습니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
