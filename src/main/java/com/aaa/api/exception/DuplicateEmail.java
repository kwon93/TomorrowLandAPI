package com.aaa.api.exception;


public class DuplicateEmail extends TomorrowException {

    private final static String MESSAGE = "DB에 이미 중복되는 이메일이 존재";
    private final static String USER_MESSAGE = "이미 존재하는 이메일 입니다.";



    public DuplicateEmail() {
        super(MESSAGE, USER_MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
