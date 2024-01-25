package com.aaa.api.exception;

public class NegativeScoreException extends TomorrowException {
    private static final String MESSAGE = "추천수를 0 이하로 내릴 수 없습니다.";
    public NegativeScoreException() {
        super(MESSAGE);
    }

    public NegativeScoreException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
