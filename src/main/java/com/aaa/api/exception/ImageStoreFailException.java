package com.aaa.api.exception;

public class ImageStoreFailException extends TomorrowException {

    private static final String MESSAGE = "이미지 바이너리 파일 저장에 실패";
    private static final String USER_MESSAGE = "이미지 파일 저장에 실패했습니다. 이미지를 다시 확인 후 업로드 해주세요.";

    public ImageStoreFailException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public ImageStoreFailException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 0;
    }
}
