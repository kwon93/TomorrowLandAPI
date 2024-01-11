package com.aaa.api.controller;

import com.aaa.api.exception.dto.ExceptionResponse;
import com.aaa.api.exception.AAAException;
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
    public ExceptionResponse methodArgsHandler(MethodArgumentNotValidException e){
        ExceptionResponse response = ExceptionResponse.builder()
                .code("400")
                .errorMessage("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse illegalArgHandler(IllegalArgumentException e){
        ExceptionResponse response = ExceptionResponse.builder()
                .code("400")
                .errorMessage("올바른 데이터를 입력하여 전송해주세요. IllegalArgumentException")
                .build();

            response.addValidation("Args",e.getMessage());
        return response;
    }

    @ResponseBody
    @ExceptionHandler(AAAException.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(AAAException e){

        ExceptionResponse response = ExceptionResponse.builder()
                .code(String.valueOf(e.statusCode()))
                .errorMessage(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(e.statusCode()).body(response);
    }
}
