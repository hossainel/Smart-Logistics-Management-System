package com.example.smartlogisticssystem.model;

public class Driver {
    private int driverId;
    private String name;
    private String phone;

    public Driver(int driverId, String name, String phone) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
    }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}