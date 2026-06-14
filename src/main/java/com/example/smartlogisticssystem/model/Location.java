package com.example.smartlogisticssystem.model;

public class Location {
    private int locationId;
    private String locationName;
    private String city;
    private String stateRegion;
    private String type; // Hub, Warehouse, Sorting Center, Drop-off Point

    public Location(int locationId, String locationName, String city, String stateRegion, String type) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.city = city;
        this.stateRegion = stateRegion;
        this.type = type;
    }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStateRegion() { return stateRegion; }
    public void setStateRegion(String stateRegion) { this.stateRegion = stateRegion; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}