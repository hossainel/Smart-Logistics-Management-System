package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.BillingManager;
import com.example.smartlogisticssystem.model.ShipmentBilling;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BillingController extends BaseController {

    @FXML private TextField trackingIdField;
    @FXML private TextField customerIdField;
    @FXML private TextField fareField;
    @FXML private ComboBox<String> methodCombo;
    @FXML private TableView<ShipmentBilling> billingTable;
    @FXML private TableColumn<ShipmentBilling, Integer> colInvoiceId;
    @FXML private TableColumn<ShipmentBilling, String> colTrackId;
    @FXML private TableColumn<ShipmentBilling, Double> colAmount;
    @FXML private TableColumn<ShipmentBilling, String> colStatus;

    private final TableColumn<ShipmentBilling, Void> colActions = new TableColumn<>("Actions");
    private final BillingManager billingManager = new BillingManager();

    @FXML
    public void initialize() {
        // Populate the dropdown selector widget items
        methodCombo.setItems(FXCollections.observableArrayList("Cash", "Credit Card", "Mobile Banking", "COD"));

        colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        colTrackId.setCellValueFactory(new PropertyValueFactory<>("trackingId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        colActions.setPrefWidth(130);
        billingTable.getColumns().add(colActions);

        addActionsToTable(colActions,
                billing -> {
                    // Edit Callback: Load selected invoice into text fields for financial recalculations
                    trackingIdField.setText(billing.getTrackingId());
                    fareField.setText(String.valueOf(billing.getTotalAmount()));
                },
                billing -> {
                    // Delete Callback: Standard accounting rollbacks
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Void Invoice ID " + billing.getInvoiceId() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // billingManager.voidInvoice(billing.getInvoiceId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleCreateInvoice() {
        try {
            String trk = trackingIdField.getText();
            int custId = Integer.parseInt(customerIdField.getText());
            double fare = Double.parseDouble(fareField.getText());
            String method = methodCombo.getValue();

            if (billingManager.createInvoice(trk, custId, fare, fare * 0.05, 0.0, method)) {
                clearFields(trackingIdField, customerIdField, fareField);
                methodCombo.setValue(null);
                refreshTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Processing Failure", null, "Database transaction declined.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format Error", null, "Please enter valid numbers for Customer ID and Fare.");
        }
    }

    private void refreshTable() {
        populateTable(billingTable, billingManager.getAllBillings());
    }
}