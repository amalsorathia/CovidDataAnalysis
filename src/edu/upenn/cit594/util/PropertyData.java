package edu.upenn.cit594.util;

public class PropertyData {
    private String zipCode;
    private double marketValue;
    private double totalLivableArea;

    // Constructor
    public PropertyData(String zipCode, double marketValue, double totalLivableArea) {
        this.zipCode = zipCode;
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
    }

    // Getters
    public String getZipCode() {
        return zipCode;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getTotalLivableArea() {
        return totalLivableArea;
    }

}