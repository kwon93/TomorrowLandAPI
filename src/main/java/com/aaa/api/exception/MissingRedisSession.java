package com.aaa.api.exception;

public class MissingRedisSession extends TomorrowException{

    private static final String MESSAGE = "Redis에 세션이 존재하지않음";
    private static final String USER_MESSAGE = "서버오류입니다. 관리자에게 문의해주세요.";


    public MissingRedisSession() {
        super(MESSAGE, USER_MESSAGE);
    }

    public MissingRedisSession(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
