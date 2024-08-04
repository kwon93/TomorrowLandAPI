package com.aaa.api.exception;

public class CommentNotFound extends TomorrowException {

    private static final String MESSAGE = "DB에서 요청한 댓글을 찾을 수 없음";
    private static final String USER_MESSAGE = "해당 댓글을 찾을 수 없습니다.";

    public CommentNotFound() {
        super(MESSAGE, USER_MESSAGE);
    }
    public CommentNotFound(String message,String userMessage, Throwable cause) {
        super(message,userMessage,cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
