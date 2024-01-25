package com.aaa.api.exception;

public class InvalidRequest extends TomorrowException {

    private static final String MESSAGE = "사용자의 잘못된 요청";
    private static final String USER_MESSAGE = "잘못된 요청입니다 다시 시도해주세요.";

    private  String fieldName;
    private  String message;


    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE,USER_MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
