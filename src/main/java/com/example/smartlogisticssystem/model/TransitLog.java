package com.example.smartlogisticssystem.model;

import java.sql.Timestamp;

public class TransitLog {
    private int logId;
    private String trackingId;
    private int currentLocationId;
    private String status;
    private Timestamp timestamp;
    private String remarks;

    public TransitLog(int logId, String trackingId, int currentLocationId, String status, Timestamp timestamp, String remarks) {
        this.logId = logId;
        this.trackingId = trackingId;
        this.currentLocationId = currentLocationId;
        this.status = status;
        this.timestamp = timestamp;
        this.remarks = remarks;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public int getCurrentLocationId() { return currentLocationId; }
    public void setCurrentLocationId(int currentLocationId) { this.currentLocationId = currentLocationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}