package com.aaa.api.exception;

public class PostNotfound extends TomorrowException {

    private final static String MESSAGE = "존재하지않는 게시물 참조";
    private final static String USER_MESSAGE = "존재하지않는 게시물입니다.";

    public PostNotfound() {
        super(MESSAGE,USER_MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
