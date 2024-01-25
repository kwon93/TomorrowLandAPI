package com.aaa.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TomorrowException extends RuntimeException{

    private final Map<String ,String > validation = new HashMap<>();

    public TomorrowException(String message) {
        super(message);
    }

    public TomorrowException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    };
}
