package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.FleetAssignmentManager;
import com.example.smartlogisticssystem.model.FleetAssignment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FleetAssignmentController extends BaseController {

    @FXML private TextField vehicleIdField;
    @FXML private TextField driverIdField;
    @FXML private ComboBox<String> statusCombo;

    @FXML private TableView<FleetAssignment> assignmentTable;
    @FXML private TableColumn<FleetAssignment, Integer> colAssignmentId;
    @FXML private TableColumn<FleetAssignment, Integer> colVehicleId;
    @FXML private TableColumn<FleetAssignment, Integer> colDriverId;
    @FXML private TableColumn<FleetAssignment, String> colStatus;

    private final TableColumn<FleetAssignment, Void> colActions = new TableColumn<>("Actions");
    private final FleetAssignmentManager assignmentManager = new FleetAssignmentManager();
    private FleetAssignment selectedAssignmentForEdit = null;

    @FXML
    public void initialize() {
        statusCombo.setItems(FXCollections.observableArrayList("Active", "On Break", "Suspended"));

        colAssignmentId.setCellValueFactory(new PropertyValueFactory<>("assignmentId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colDriverId.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colActions.setPrefWidth(130);
        assignmentTable.getColumns().add(colActions);

        // Uses your BaseController actions factory smoothly
        addActionsToTable(colActions,
                assignment -> {
                    this.selectedAssignmentForEdit = assignment;
                    vehicleIdField.setText(String.valueOf(assignment.getVehicleId()));
                    driverIdField.setText(String.valueOf(assignment.getDriverId()));
                    statusCombo.setValue(assignment.getStatus());
                },
                assignment -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "De-activate partnership pairing link?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            assignmentManager.updateAssignmentStatus(assignment.getAssignmentId(), "Terminated");
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleSaveAssignment() {
        try {
            int vId = Integer.parseInt(vehicleIdField.getText().trim());
            int dId = Integer.parseInt(driverIdField.getText().trim());
            String status = statusCombo.getValue();

            if (status == null) {
                showAlert(Alert.AlertType.WARNING, "Missing Selection", null, "Please assign a deployment operational status.");
                return;
            }

            if (assignmentManager.assignFleet(vId, dId, status)) {
                clearFields(vehicleIdField, driverIdField);
                statusCombo.setValue(null);
                selectedAssignmentForEdit = null;
                refreshTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Write Rejection", null, "SQL structural constraints declined pairing registration.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Parse Violation", null, "Vehicle and Driver fields require plain integer values.");
        }
    }

    private void refreshTable() {
        populateTable(assignmentTable, assignmentManager.getAllAssignments());
    }
}