package com.aaa.api.exception;

public class FileNotExistException extends TomorrowException {

    private static final String MESSAGE = "로컬 디스크에 존재하지않는 파일 삭제";
    private static final String USER_MESSAGE = "삭제하려는 게시물에 이미지가 존재하지 않습니다.";

    public FileNotExistException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public FileNotExistException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
