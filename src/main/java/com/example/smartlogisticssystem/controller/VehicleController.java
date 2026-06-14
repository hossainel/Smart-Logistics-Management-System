package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.VehicleManager;
import com.example.smartlogisticssystem.model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class VehicleController extends BaseController {

    @FXML private TextField vehicleTypeField;
    @FXML private TextField plateNoField;
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> colId;
    @FXML private TableColumn<Vehicle, String> colType;
    @FXML private TableColumn<Vehicle, String> colPlate;

    private final TableColumn<Vehicle, Void> colActions = new TableColumn<>("Actions");
    private final VehicleManager vehicleManager = new VehicleManager();
    private Vehicle selectedVehicleForEdit = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPlate.setCellValueFactory(new PropertyValueFactory<>("plateNo"));

        colActions.setPrefWidth(130);
        vehicleTable.getColumns().add(colActions);

        addActionsToTable(colActions,
                vehicle -> {
                    this.selectedVehicleForEdit = vehicle;
                    vehicleTypeField.setText(vehicle.getType());
                    plateNoField.setText(vehicle.getPlateNo());
                },
                vehicle -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Remove asset " + vehicle.getPlateNo() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // vehicleManager.deleteVehicle(vehicle.getVehicleId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleAddVehicle() {
        String type = vehicleTypeField.getText();
        String plate = plateNoField.getText();

        if (vehicleManager.registerVehicle(type, plate)) {
            clearFields(vehicleTypeField, plateNoField);
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Failed to register vehicle profile data.");
        }
    }

    private void refreshTable() {
        populateTable(vehicleTable, vehicleManager.getAllVehicles());
    }
}