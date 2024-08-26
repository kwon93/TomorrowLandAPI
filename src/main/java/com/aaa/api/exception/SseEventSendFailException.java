package com.aaa.api.exception;

public class SseEventSendFailException extends TomorrowException {

    private final static String MESSAGE = "SSE Event 전송 실패";
    private final static String USER_MESSAGE = "죄송합니다. 알림 서비스 네트워크에 문제가 발생했습니다.";

    public SseEventSendFailException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public SseEventSendFailException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 500;
    }
}
