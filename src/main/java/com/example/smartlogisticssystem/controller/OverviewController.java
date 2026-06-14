package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.ParcelManager;
import com.example.smartlogisticssystem.manager.VehicleManager;
import com.example.smartlogisticssystem.manager.DriverManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OverviewController extends BaseController {

    @FXML private Label totalParcelsLabel;
    @FXML private Label activeVehiclesLabel;
    @FXML private Label activeDriversLabel;

    private final ParcelManager parcelManager = new ParcelManager();
    private final VehicleManager vehicleManager = new VehicleManager();
    private final DriverManager driverManager = new DriverManager();

    @FXML
    public void initialize() {
        // Collect aggregates from managers and populate labels safely
        totalParcelsLabel.setText(String.valueOf(parcelManager.getAllParcels().size()));
        activeVehiclesLabel.setText(String.valueOf(vehicleManager.getAllVehicles().size()));
        activeDriversLabel.setText(String.valueOf(driverManager.getAllDrivers().size()));
    }
}