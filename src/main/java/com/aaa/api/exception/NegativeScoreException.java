package com.aaa.api.exception;

public class NegativeScoreException extends TomorrowException {
    private static final String MESSAGE = "최소 추천수 0 미만 오류";
    private static final String USER_MESSAGE = "추천수를 0 이하로 내릴 수 없습니다.";
    public NegativeScoreException() {
        super(MESSAGE,USER_MESSAGE);
    }

    public NegativeScoreException(Throwable cause) {
        super(MESSAGE,USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
