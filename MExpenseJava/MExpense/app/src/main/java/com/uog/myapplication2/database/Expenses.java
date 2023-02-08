package com.uog.myapplication2.database;

public class Expenses {

    private Integer id;
    private Integer tripId;
    private String expenseType;
    private Double amount;
    private long expenseTime;
    private String comment;
    private String value1;
    private String value2;
    private String value3;

    public Expenses() {
    }

    public Expenses(Integer tripId, String expenseType, Double amount, long expenseTime, String comment, String value1, String value2, String value3) {
        this.tripId = tripId;
        this.expenseType = expenseType;
        this.amount = amount;
        this.expenseTime = expenseTime;
        this.comment = comment;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public Expenses(Integer id, Integer tripId, String expenseType, Double amount, long expenseTime, String comment, String value1, String value2, String value3) {
        this.id = id;
        this.tripId = tripId;
        this.expenseType = expenseType;
        this.amount = amount;
        this.expenseTime = expenseTime;
        this.comment = comment;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
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

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }
}
