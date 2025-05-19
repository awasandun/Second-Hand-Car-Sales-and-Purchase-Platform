package com.se1020.secondhandcarplatform.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Bid implements Serializable {
    private String id;
    private String carId;
    private String bidderId;
    private double amount;
    private LocalDateTime bidTime;
    private String status; // "PENDING", "ACCEPTED", "REJECTED"

    // Default constructor
    public Bid() {
        this.id = UUID.randomUUID().toString();
        this.bidTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Parameterized constructor
    public Bid(String carId, String bidderId, double amount) {
        this.id = UUID.randomUUID().toString();
        this.carId = carId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.bidTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + carId + "," + bidderId + "," + amount + "," + bidTime + "," + status;
    }
}
