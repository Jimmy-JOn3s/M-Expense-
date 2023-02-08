package com.uog.myapplication2.database;

public class TripExportData {
    private String name;
    private String destination;
    private long date;
    private String expenseType;
    private Double amount;
    private long expenseTime;
    private String comment;

    public TripExportData() {
    }

    public TripExportData(String name, String destination, long date, String expenseType, Double amount, long expenseTime, String comment) {
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.expenseType = expenseType;
        this.amount = amount;
        this.expenseTime = expenseTime;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getExpenseTime() {
        return expenseTime;
    }

    public void setExpenseTime(long expenseTime) {
        this.expenseTime = expenseTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
