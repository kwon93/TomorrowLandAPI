package com.aaa.api.controller;

import com.aaa.api.exception.dto.ExceptionResponse;
import com.aaa.api.exception.TomorrowException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse methodArgsHandler(MethodArgumentNotValidException exception){
        ExceptionResponse response = buildDefaultExceptionResponse();
        addValidationToResponse(exception, response);
        return response;
    }

    @ResponseBody
    @ExceptionHandler(TomorrowException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(TomorrowException tomorrowException){
        ExceptionResponse response = buildTomorrowExceptionResponse(tomorrowException);
        return ResponseEntity.status(tomorrowException.statusCode()).body(response);
    }

    private static ExceptionResponse buildDefaultExceptionResponse() {
        return ExceptionResponse.builder()
                .code("400")
                .errorMessage("잘못된 요청입니다.")
                .build();
    }

    private static ExceptionResponse buildTomorrowExceptionResponse(TomorrowException tomorrowException) {
        return ExceptionResponse.builder()
                .code(String.valueOf(tomorrowException.statusCode()))
                .errorMessage(tomorrowException.getMessage())
                .validation(tomorrowException.getValidation())
                .userMessage(tomorrowException.getUserMessage())
                .build();
    }

    private static void addValidationToResponse(MethodArgumentNotValidException e, ExceptionResponse response) {
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
