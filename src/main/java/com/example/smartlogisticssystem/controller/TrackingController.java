package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.TransitLogManager;
import com.example.smartlogisticssystem.model.TransitLog;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TrackingController extends BaseController {

    @FXML private TextField trackingIdField;
    @FXML private Label statusLabel;
    @FXML private TableView<TransitLog> transitLogsTable;
    @FXML private TableColumn<TransitLog, String> colTime;
    @FXML private TableColumn<TransitLog, String> colStatus;
    @FXML private TableColumn<TransitLog, String> colRemarks;

    private final TransitLogManager logManager = new TransitLogManager();

    @FXML
    public void initialize() {
        colTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRemarks.setCellValueFactory(new PropertyValueFactory<>("remarks"));
    }

    @FXML
    private void trackParcel() {
        String trkId = trackingIdField.getText().trim();
        var logsList = logManager.getAuditTrailForParcel(trkId);

        if (!logsList.isEmpty()) {
            statusLabel.setText(logsList.get(logsList.size() - 1).getStatus()); // Pull latest milestone status string
            populateTable(transitLogsTable, logsList);
        } else {
            statusLabel.setText("No log trail records available.");
            transitLogsTable.getItems().clear();
            showAlert(Alert.AlertType.INFORMATION, "Not Found", null, "Tracking reference does not exist.");
        }
    }
}