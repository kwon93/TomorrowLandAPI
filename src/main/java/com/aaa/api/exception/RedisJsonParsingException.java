package com.aaa.api.exception;

public class RedisJsonParsingException extends TomorrowException{

    private final static String MESSAGE = "레디스 데이터 제이슨 파싱 실패.";
    private final static String USER_MESSAGE = "알림을 가져오는데 문제가 발생했습니다.";

    public RedisJsonParsingException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public RedisJsonParsingException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
