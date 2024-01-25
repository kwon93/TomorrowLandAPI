package com.aaa.api.exception.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

    private LocalDateTime timeStamp;
    private String code;
    private String errorMessage;
    private Map<String , String > validation = new HashMap<>();

    @Builder
    public ExceptionResponse(String code, String errorMessage, Map<String ,String > validation) {
        this.timeStamp =  LocalDateTime.now();
        this.validation = validation != null ? validation : new HashMap<>();
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public void addValidation(String field, String defaultMessage) {
        validation.put(field,defaultMessage);
    }

}
