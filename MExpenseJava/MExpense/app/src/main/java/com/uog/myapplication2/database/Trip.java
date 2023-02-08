package com.uog.myapplication2.database;

public class Trip {
    private Integer id;
    private String name;
    private String destination;
    private long date;
    private boolean riskAssessment;
    private String description;
    private String value1;
    private String value2;
    private String value3;
    private Double num1;
    private Double num2;

    public Trip() {
    }

    public Trip(Integer id, String name, String destination, long date, boolean riskAssessment, String description, String value1, String value2, String value3, Double num1, Double num2) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.date = date;
        this.riskAssessment = riskAssessment;
        this.description = description;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.num1 = num1;
        this.num2 = num2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(boolean riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getNum1() {
        return num1;
    }

    public void setNum1(Double num1) {
        this.num1 = num1;
    }

    public Double getNum2() {
        return num2;
    }

    public void setNum2(Double num2) {
        this.num2 = num2;
    }
}
