package com.ericsson.dev.domain;

import java.util.Comparator;

public class Discounts {
    private String planName;
    private String rut;
    private String cuenta;
    private String contrato;
    private int position;
    private double price;
    private double percentage;
    private int factor;
    private boolean newCycle;

    public boolean isNewCycle() {
        return newCycle;
    }

    public void setNewCycle(boolean newCycle) {
        this.newCycle = newCycle;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }


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

    public void setPrice(double price) {
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
