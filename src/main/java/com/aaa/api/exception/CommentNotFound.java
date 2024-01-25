package com.aaa.api.exception;

public class CommentNotFound extends TomorrowException {

    private final static String MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public CommentNotFound() {
        super(MESSAGE);
    }
    public CommentNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
