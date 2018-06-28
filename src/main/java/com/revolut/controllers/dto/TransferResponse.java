package com.revolut.controllers.dto;

import java.math.BigDecimal;

public class TransferResponse extends BaseResponse {

    private String message;
    private Long fromUserId;
    private BigDecimal fromUserBalance;
    private Long toUserId;
    private BigDecimal toUserBalance;
    private BigDecimal transferredAmount;

    public TransferResponse(String message, Long fromUserId, BigDecimal fromUserBalance, Long toUserId, BigDecimal toUserBalance, BigDecimal transferredAmount) {
        this.message = message;
        this.fromUserId = fromUserId;
        this.fromUserBalance = fromUserBalance;
        this.toUserId = toUserId;
        this.toUserBalance = toUserBalance;
        this.transferredAmount = transferredAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public BigDecimal getFromUserBalance() {
        return fromUserBalance;
    }

    public void setFromUserBalance(BigDecimal fromUserBalance) {
        this.fromUserBalance = fromUserBalance;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getToUserBalance() {
        return toUserBalance;
    }

    public void setToUserBalance(BigDecimal toUserBalance) {
        this.toUserBalance = toUserBalance;
    }

    public BigDecimal getTransferredAmount() {
        return transferredAmount;
    }

    public void setTransferredAmount(BigDecimal transferredAmount) {
        this.transferredAmount = transferredAmount;
    }

    @Override
    public String toString() {
        return "TransferResponse{" +
                "message='" + message + '\'' +
                ", fromUserId=" + fromUserId +
                ", fromUserBalance=" + fromUserBalance +
                ", toUserId=" + toUserId +
                ", toUserBalance=" + toUserBalance +
                ", transferredAmount=" + transferredAmount +
                '}';
    }
}
