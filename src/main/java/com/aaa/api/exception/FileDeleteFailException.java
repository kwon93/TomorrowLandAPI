package com.aaa.api.exception;

public class FileDeleteFailException extends TomorrowException {

    private static final String MESSAGE = "파일 삭제중 오류 발생";
    private static final String USER_MESSAGE = "이미지 삭제에 실패했습니다.";

    public FileDeleteFailException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public FileDeleteFailException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
