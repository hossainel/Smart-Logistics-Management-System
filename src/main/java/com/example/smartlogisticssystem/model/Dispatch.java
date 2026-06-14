package com.example.smartlogisticssystem.model;

import java.sql.Timestamp;

public class Dispatch {
    private int dispatchId;
    private int vehicleId;
    private int driverId;
    private int originLocationId;
    private int destinationLocationId;
    private Timestamp departureTime;
    private Timestamp estimatedArrival;
    private Timestamp actualArrival;
    private String dispatchStatus;

    // Original 9-parameter constructor
    public Dispatch(int dispatchId, int vehicleId, int driverId, int originLocationId, int destinationLocationId, Timestamp departureTime, Timestamp estimatedArrival, Timestamp actualArrival, String dispatchStatus) {
        this.dispatchId = dispatchId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.originLocationId = originLocationId;
        this.destinationLocationId = destinationLocationId;
        this.departureTime = departureTime;
        this.estimatedArrival = estimatedArrival;
        this.actualArrival = actualArrival;
        this.dispatchStatus = dispatchStatus;
    }

    // ADD THIS: Overloaded 8-parameter constructor for safety/flexibility
    public Dispatch(int dispatchId, int vehicleId, int driverId, int originLocationId, int destinationLocationId, Timestamp departureTime, Timestamp estimatedArrival, String dispatchStatus) {
        this(dispatchId, vehicleId, driverId, originLocationId, destinationLocationId, departureTime, estimatedArrival, null, dispatchStatus);
    }

    // ADD THIS: UI Helper Method maps perfectly to PropertyValueFactory("routePath")
    public String getRoutePath() {
        return "Loc " + originLocationId + " → Loc " + destinationLocationId;
    }

    // Getters and Setters
    public int getDispatchId() { return dispatchId; }
    public void setDispatchId(int dispatchId) { this.dispatchId = dispatchId; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public int getOriginLocationId() { return originLocationId; }
    public void setOriginLocationId(int originLocationId) { this.originLocationId = originLocationId; }
    public int getDestinationLocationId() { return destinationLocationId; }
    public void setDestinationLocationId(int destinationLocationId) { this.destinationLocationId = destinationLocationId; }
    public Timestamp getDepartureTime() { return departureTime; }
    public void setDepartureTime(Timestamp departureTime) { this.departureTime = departureTime; }
    public Timestamp getEstimatedArrival() { return estimatedArrival; }
    public void setEstimatedArrival(Timestamp estimatedArrival) { this.estimatedArrival = estimatedArrival; }
    public Timestamp getActualArrival() { return actualArrival; }
    public void setActualArrival(Timestamp actualArrival) { this.actualArrival = actualArrival; }
    public String getDispatchStatus() { return dispatchStatus; }
    public void setDispatchStatus(String dispatchStatus) { this.dispatchStatus = dispatchStatus; }
}