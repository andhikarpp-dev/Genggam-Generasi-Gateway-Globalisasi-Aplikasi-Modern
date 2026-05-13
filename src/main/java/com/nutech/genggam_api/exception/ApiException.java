package com.nutech.genggam_api.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final int statusCode;
    private final HttpStatus httpStatus;

    public ApiException(int statusCode, HttpStatus httpStatus, String message) {
        super(message);
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
    }

    public int getStatusCode() { return statusCode; }
    public HttpStatus getHttpStatus() { return httpStatus; }

    public static ApiException badRequest(String message) {
        return new ApiException(102, HttpStatus.BAD_REQUEST, message);
    }
    public static ApiException invalidCredential(String message) {
        return new ApiException(103, HttpStatus.UNAUTHORIZED, message);
    }
    public static ApiException unauthorized(String message) {
        return new ApiException(108, HttpStatus.UNAUTHORIZED, message);
    }
}
