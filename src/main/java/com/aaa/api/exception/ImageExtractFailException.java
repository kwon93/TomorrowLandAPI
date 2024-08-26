package com.aaa.api.exception;

public class ImageExtractFailException extends TomorrowException {

    private static final String MESSAGE = "바이트 파일 가져오는 도중 에러 발생.";
    private static final String USER_MESSAGE = "이미지를 가져오는데에 실패했습니다.";

    public ImageExtractFailException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public ImageExtractFailException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
