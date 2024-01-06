package com.aaa.api.exception.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ExceptionResponse {

    private String code;
    private String errorMessage;
    private Map<String , String > validation = new HashMap<>();

    @Builder
    public ExceptionResponse(String code, String errorMessage,Map<String ,String > validation) {
        this.validation = validation != null ? validation : new HashMap<>();
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public void addValidation(String field, String defaultMessage) {
        validation.put(field,defaultMessage);
    }

}
