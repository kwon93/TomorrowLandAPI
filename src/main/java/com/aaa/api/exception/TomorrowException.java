package com.aaa.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TomorrowException extends RuntimeException{

    private final Map<String ,String > validation = new HashMap<>();
    private final String userMessage;

    public TomorrowException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public TomorrowException(String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    };
}
