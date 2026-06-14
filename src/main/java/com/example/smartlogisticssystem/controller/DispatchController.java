package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.DispatchManager;
import com.example.smartlogisticssystem.model.Dispatch;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Timestamp;

public class DispatchController extends BaseController {

    @FXML private TextField vehicleIdField;
    @FXML private TextField driverIdField;
    @FXML private TextField originIdField;
    @FXML private TextField destinationIdField;
    @FXML private TextField departureTimeField;
    @FXML private TextField arrivalTimeField;

    @FXML private TableView<Dispatch> dispatchTable;
    @FXML private TableColumn<Dispatch, Integer> colDispatchId;
    @FXML private TableColumn<Dispatch, Integer> colVehicleId;
    @FXML private TableColumn<Dispatch, Integer> colDriverId;
    @FXML private TableColumn<Dispatch, String> colRoute;
    @FXML private TableColumn<Dispatch, String> colStatus;

    private final TableColumn<Dispatch, Void> colActions = new TableColumn<>("Actions");
    private final DispatchManager dispatchManager = new DispatchManager();

    @FXML
    public void initialize() {
        // Bind UI TableColumns to corresponding fields or getter properties inside Dispatch.java
        colDispatchId.setCellValueFactory(new PropertyValueFactory<>("dispatchId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colDriverId.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        colRoute.setCellValueFactory(new PropertyValueFactory<>("routePath")); // Maps to getRoutePath() helper method
        colStatus.setCellValueFactory(new PropertyValueFactory<>("dispatchStatus"));

        colActions.setPrefWidth(130);
        dispatchTable.getColumns().add(colActions);

        // Standard dynamic actions handling hook implementation
        addActionsToTable(colActions,
                dispatch -> {
                    // Edit Hook: Populate entry fields for record refinement
                    vehicleIdField.setText(String.valueOf(dispatch.getVehicleId()));
                    driverIdField.setText(String.valueOf(dispatch.getDriverId()));
                    originIdField.setText(String.valueOf(dispatch.getOriginLocationId()));
                    destinationIdField.setText(String.valueOf(dispatch.getDestinationLocationId()));
                    departureTimeField.setText(dispatch.getDepartureTime().toString());
                    arrivalTimeField.setText(dispatch.getEstimatedArrival().toString());
                },
                dispatch -> {
                    // Delete Hook: Safely strip record row down in backend
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Delete Dispatch manifest order " + dispatch.getDispatchId() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            if (dispatchManager.deleteDispatch(dispatch.getDispatchId())) {
                                refreshTable();
                            } else {
                                showAlert(Alert.AlertType.ERROR, "Error", null, "Unable to delete dispatch manifest line.");
                            }
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleCreateDispatch() {
        try {
            int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
            int driverId = Integer.parseInt(driverIdField.getText().trim());
            int originId = Integer.parseInt(originIdField.getText().trim());
            int destId = Integer.parseInt(destinationIdField.getText().trim());

            // Process text parameters into validated SQL timestamp records
            Timestamp departure = Timestamp.valueOf(departureTimeField.getText().trim());
            Timestamp arrival = Timestamp.valueOf(arrivalTimeField.getText().trim());

            if (dispatchManager.createDispatch(vehicleId, driverId, originId, destId, departure, arrival)) {
                clearFields(vehicleIdField, driverIdField, originIdField, destinationIdField, departureTimeField, arrivalTimeField);
                refreshTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Operational Failure", null,
                        "Database operational error creating the route run manifest.");
            }
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Data Parse Exception", "Invalid Form Data Format",
                    "Ensure numerical IDs are integers and dates match pattern: YYYY-MM-DD HH:MM:SS");
        }
    }

    private void refreshTable() {
        // CHANGED: Fixed the hardcoded empty collection data bug to select live database outputs instead
        populateTable(dispatchTable, dispatchManager.getAllDispatches());
    }
}