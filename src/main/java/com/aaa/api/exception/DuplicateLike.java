package com.aaa.api.exception;

public class DuplicateLike extends TomorrowException {
    private static final String MESSAGE = "사용자의 중복 추천 게시물";
    private static final String USER_MESSAGE = "이미 추천한 게시물입니다.";
    public DuplicateLike() {
        super(MESSAGE,USER_MESSAGE);
    }

    public DuplicateLike(Throwable cause) {
        super(MESSAGE,USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
