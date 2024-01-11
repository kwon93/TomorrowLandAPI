package com.aaa.api.exception;

public class DuplicateLike extends AAAException {
    private static final String MESSAGE = "이미 추천한 게시물입니다.";
    public DuplicateLike() {
        super(MESSAGE);
    }

    public DuplicateLike(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
