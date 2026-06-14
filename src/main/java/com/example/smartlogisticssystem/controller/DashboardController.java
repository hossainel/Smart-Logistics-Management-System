package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.MainApplication;
import com.example.smartlogisticssystem.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class DashboardController extends BaseController {

    // Matches the fx:id assigned to the central BorderPane component wrapper in Dashboard.fxml
    @FXML
    private BorderPane mainBorderPane;

    /**
     * The initialize method runs automatically after the FXML file is loaded.
     * We use this hook to populate the canvas center frame with a default home screen overview.
     */
    @FXML
    public void initialize() {
        // Loads a default startup view into the middle frame workspace via initial selection execution path
        handleShowOverview();
    }

    /**
     * Sidebar Actions - Swapping target feature panels into view on click
     */

    @FXML
    private void handleShowOverview() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "OverviewPanel.fxml");
    }

    @FXML
    private void handleShowParcels() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "ParcelPanel.fxml");
    }

    @FXML
    private void handleShowCustomers() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "CustomerPanel.fxml");
    }

    @FXML
    private void handleShowWarehouses() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "WarehousePanel.fxml");
    }

    @FXML
    private void handleShowLocations() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "LocationPanel.fxml");
    }

    @FXML
    private void handleShowDrivers() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "DriverPanel.fxml");
    }

    @FXML
    private void handleShowVehicles() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "VehiclePanel.fxml");
    }

    @FXML
    private void handleShowAssignments() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "FleetAssignmentPanel.fxml");
    }

    @FXML
    private void handleShowDispatches() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "DispatchPanel.fxml");
    }

    @FXML
    private void handleShowBilling() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "BillingPanel.fxml");
    }

    @FXML
    private void handleShowTracking() {
        SceneNavigator.loadCenterPanel(mainBorderPane, "TrackingPanel.fxml");
    }

    /**
     * Closes the active administrative session and returns the window frame back to the core Login portal.
     */
    @FXML
    private void handleLogout() {
        // Drop the dashboard interface wrapper state and refresh the primary screen
        MainApplication.showLoginView();
    }
}
