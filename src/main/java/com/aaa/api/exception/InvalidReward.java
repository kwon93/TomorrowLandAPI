package com.aaa.api.exception;

public class InvalidReward extends TomorrowException {
    private static final String MESSAGE = "본인에게 점수 주기 방지 오류";
    private static final String USER_MESSAGE = "본인 댓글에는 점수를 줄 수 없습니다.";


    public InvalidReward() {
        super(MESSAGE, USER_MESSAGE);
    }

    public InvalidReward(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
