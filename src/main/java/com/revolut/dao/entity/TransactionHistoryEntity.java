package com.revolut.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "account_history")
public class TransactionHistoryEntity {

    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "from_user")
    private Long fromUser;
    @Column(name = "to_user")
    private Long toUser;
    @Column(name = "previous_from_user_balance")
    private BigDecimal previousFromUserBalance;
    @Column(name = "previous_to_user_balance")
    private BigDecimal previousToUserBalance;
    @Column(name = "current_from_user_balance")
    private BigDecimal currentFromUserBalance;
    @Column(name = "current_to_user_balance")
    private BigDecimal currentToUserBalance;
    @Column(name = "amount")
    private BigDecimal amount;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToUser() {
        return toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getPreviousFromUserBalance() {
        return previousFromUserBalance;
    }

    public void setPreviousFromUserBalance(BigDecimal previousFromUserBalance) {
        this.previousFromUserBalance = previousFromUserBalance;
    }

    public BigDecimal getPreviousToUserBalance() {
        return previousToUserBalance;
    }

    public void setPreviousToUserBalance(BigDecimal previousToUserBalance) {
        this.previousToUserBalance = previousToUserBalance;
    }

    public BigDecimal getCurrentFromUserBalance() {
        return currentFromUserBalance;
    }

    public void setCurrentFromUserBalance(BigDecimal currentFromUserBalance) {
        this.currentFromUserBalance = currentFromUserBalance;
    }

    public BigDecimal getCurrentToUserBalance() {
        return currentToUserBalance;
    }

    public void setCurrentToUserBalance(BigDecimal currentToUserBalance) {
        this.currentToUserBalance = currentToUserBalance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
