package com.se1020.secondhandcarplatform.model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role; // "ADMIN" or "USER"

    // Default constructor
    public User() {
        this.id = UUID.randomUUID().toString();
    }

    // Parameterized constructor
    public User(String username, String password, String email, String phone, String role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return id + "," + username + "," + password + "," + email + "," + phone + "," + role;
    }
}
