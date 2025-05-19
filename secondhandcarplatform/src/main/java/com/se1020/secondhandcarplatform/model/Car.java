package com.se1020.secondhandcarplatform.model;

import java.io.Serializable;
import java.util.UUID;

public class Car implements Serializable {
    private String id;
    private String make;
    private String model;
    private int year;
    private double price;
    private String description;
    private String sellerId;
    private String condition;
    private int mileage;
    private String imageUrl;
    private boolean sold;

    // Default constructor
    public Car() {
        this.id = UUID.randomUUID().toString();
        this.sold = false;
    }

    // Parameterized constructor
    public Car(String make, String model, int year, double price, String description,
               String sellerId, String condition, int mileage, String imageUrl) {
        this.id = UUID.randomUUID().toString();
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.description = description;
        this.sellerId = sellerId;
        this.condition = condition;
        this.mileage = mileage;
        this.imageUrl = imageUrl;
        this.sold = false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return id + "," + make + "," + model + "," + year + "," + price + "," +
                description + "," + sellerId + "," + condition + "," + mileage + "," +
                imageUrl + "," + sold;
    }
}
