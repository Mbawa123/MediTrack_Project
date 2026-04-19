package com.meditrack.model.record;

public class Vitals {
    private String bloodPressure;
    private double temperature;
    private double weight;

    public Vitals(String bloodPressure, double temperature, double weight) {
        this.bloodPressure = bloodPressure;
        this.temperature   = temperature;
        this.weight        = weight;
    }

    public void setTemperature(double temperature) {
        if (temperature < 30.0 || temperature > 45.0)
            throw new IllegalArgumentException("Temperature out of realistic range");
        this.temperature = temperature;
    }

    public String getBloodPressure() { return bloodPressure; }
    public double getTemperature()   { return temperature; }
    public double getWeight()        { return weight; }
    public void setBloodPressure(String bp) { this.bloodPressure = bp; }
    public void setWeight(double w)         { this.weight = w; }
}
