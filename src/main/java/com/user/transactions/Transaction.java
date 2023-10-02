package com.user.transactions;

import java.util.Date;

public class Transaction {
    private String transactionType;
    private String transactionId;
    private String transferFrom;
    private Date date;
    private double amount;

    public Transaction() {
    }

    public Transaction(String transactionType, String transactionId, String transferFrom, Date date, double amount) {
        this.transactionId = transactionId;
    	this.transactionType = transactionType;
        this.transferFrom = transferFrom;
        this.date = date;
        this.amount = amount;
    }
    
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}