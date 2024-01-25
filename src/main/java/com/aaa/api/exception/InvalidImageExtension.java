package com.aaa.api.exception;

public class InvalidImageExtension extends TomorrowException {

    private final static String MESSAGE = "png,jpg,jpeg,webp 외의 확장자명 오류";
    private final static String USER_MESSAGE = "지원하지 않는 확장자명입니다.";
    public InvalidImageExtension() {
        super(MESSAGE,USER_MESSAGE);
    }

    public InvalidImageExtension(Throwable cause) {
        super(MESSAGE,USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
