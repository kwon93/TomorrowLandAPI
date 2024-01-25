package com.aaa.api.exception;

public class InvalidRequest extends TomorrowException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    private  String fieldName;
    private  String message;


    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
