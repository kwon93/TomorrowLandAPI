package com.aaa.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AAAException extends RuntimeException{

    private final Map<String ,String > validation = new HashMap<>();

    public AAAException(String message) {
        super(message);
    }

    public AAAException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName,message);
    };
}
