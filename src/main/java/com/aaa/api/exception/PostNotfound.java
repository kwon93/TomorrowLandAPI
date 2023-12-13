package com.aaa.api.exception;

public class PostNotfound extends AAAException{

    private final static String MESSAGE = "존재하지않는 게시물입니다.";

    public PostNotfound() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
