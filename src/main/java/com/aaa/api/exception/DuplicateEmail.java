package com.aaa.api.exception;


public class DuplicateEmail extends TomorrowException {

    private final static String MESSAGE = "이미 존재하는 이메일 입니다.";

    public DuplicateEmail() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
