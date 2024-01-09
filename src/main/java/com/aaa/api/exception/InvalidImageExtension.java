package com.aaa.api.exception;

public class InvalidImageExtension extends AAAException {

    private final static String MESSAGE = "지원하지 않는 확장자명입니다.";
    public InvalidImageExtension() {
        super(MESSAGE);
    }

    public InvalidImageExtension(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
