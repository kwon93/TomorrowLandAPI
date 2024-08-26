package com.aaa.api.exception;

public class InvalidSession extends TomorrowException{

    private static final String MESSAGE = "서버에서 발행한 세션이 존재하지않거나 잘못됨.";
    private static final String USER_MESSAGE = "서버오류입니다. 관리자에게 문의해주세요.";


    public InvalidSession() {
        super(MESSAGE, USER_MESSAGE);
    }

    public InvalidSession(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
