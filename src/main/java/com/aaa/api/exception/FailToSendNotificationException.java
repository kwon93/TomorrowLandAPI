package com.aaa.api.exception;

public class FailToSendNotificationException extends TomorrowException {

    private static final String MESSAGE = "Redis에서 알림메시지 퍼블리싱중 에러발생.";
    private static final String USER_MESSAGE = "죄송합니다. 알림을 가져올 수 없습니다.";

    public FailToSendNotificationException() {
        super(MESSAGE, USER_MESSAGE);
    }

    public FailToSendNotificationException(Throwable cause) {
        super(MESSAGE, USER_MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 500;
    }
}
