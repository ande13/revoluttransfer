package com.revolut.controllers.dto;

public class BaseResponse {

    private String errorMessage;
    private Long userId;

    public BaseResponse() {
    }

    public BaseResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BaseResponse(String errorMessage, Long userId) {
        this.errorMessage = errorMessage;
        this.userId = userId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
