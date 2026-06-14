package com.example.smartlogisticssystem.model;

public class Vehicle {
    private int vehicleId;
    private String type;
    private String plateNo;

    public Vehicle(int vehicleId, String type, String plateNo) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.plateNo = plateNo;
    }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPlateNo() { return plateNo; }
    public void setPlateNo(String plateNo) { this.plateNo = plateNo; }
}
