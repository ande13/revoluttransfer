package com.revolut.exceptions;

public class TransferException extends RuntimeException {

    private Long userId;

    public TransferException(String message, Long userId) {
        super(message);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
