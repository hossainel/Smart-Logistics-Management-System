package com.example.smartlogisticssystem.model;

public class FleetAssignment {
    private int assignmentId;
    private int vehicleId;
    private int driverId;
    private String status;

    // Constructor matching the ResultSet extraction signature in FleetAssignmentManager
    public FleetAssignment(int assignmentId, int vehicleId, int driverId, String status) {
        this.assignmentId = assignmentId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.status = status;
    }

    // Getters
    public int getAssignmentId() { return assignmentId; }
    public int getVehicleId() { return vehicleId; }
    public int getDriverId() { return driverId; }
    public String getStatus() { return status; }

    // Setters for Edit operations
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public void setStatus(String status) { this.status = status; }
}