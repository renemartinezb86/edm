package com.ericsson.dev.domain;

import java.util.Comparator;

public class Discounts {
    private String planName;
    private String rut;
    private int position;
    private double price;
    private double percentage;
    private int factor;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public static class SortByPrice implements Comparator<Discounts> {
        public int compare(Discounts a, Discounts b) {
            return Double.compare(a.price, b.price);
        }
    }
}
