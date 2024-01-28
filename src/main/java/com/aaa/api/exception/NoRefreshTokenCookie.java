package com.aaa.api.exception;

public class NoRefreshTokenCookie extends TomorrowException{
    private static final String MESSAGE = "Client 요청에 RefreshToken이 존재하지 않음";
    private static final String USER_MESSAGE = "다시 로그인 해주세요. 죄송합니다!";
    public NoRefreshTokenCookie() {
        super(MESSAGE, USER_MESSAGE);
    }

    public NoRefreshTokenCookie(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
