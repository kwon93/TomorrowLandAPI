package com.aaa.api.exception;

public class DuplicateReward extends TomorrowException {

    private static final String MESSAGE = "중복 보상 방지 오류.";
    private static final String USER_MESSAGE = "보상주기는 한 댓글당 한번만 가능합니다.";


    public DuplicateReward() {
        super(MESSAGE, USER_MESSAGE);
    }

    public DuplicateReward(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
