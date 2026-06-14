package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.ParcelManager;
import com.example.smartlogisticssystem.model.Parcel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParcelController extends BaseController {

    @FXML private TextField senderField;
    @FXML private TextField receiverField;

    @FXML private TableView<Parcel> parcelTable;
    @FXML private TableColumn<Parcel, String> colTrackingId;
    @FXML private TableColumn<Parcel, String> colSender;
    @FXML private TableColumn<Parcel, String> colReceiver;
    @FXML private TableColumn<Parcel, String> colStatus;

    // Create an un-mapped UI column specifically for action buttons
    private TableColumn<Parcel, Void> colActions = new TableColumn<>("Actions");

    private final ParcelManager parcelManager = new ParcelManager();
    private Parcel selectedParcelForEdit = null; // State tracker

    @FXML
    public void initialize() {
        colTrackingId.setCellValueFactory(new PropertyValueFactory<>("trackingId"));
        colSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        colReceiver.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 1. Configure the actions column widths and append it to our layout dynamically
        colActions.setPrefWidth(130);
        parcelTable.getColumns().add(colActions);

        // 2. Attach callbacks via the BaseController helper method
        addActionsToTable(colActions, this::loadParcelIntoForm, this::processParcelDeletion);

        refreshTable();
    }

    @FXML
    private void handleAddParcel() {
        String sender = senderField.getText().trim();
        String receiver = receiverField.getText().trim();

        if (sender.isEmpty() || receiver.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Fields cannot be blank.");
            return;
        }

        if (selectedParcelForEdit == null) {
            // Logic path: Regular new creation
            if (parcelManager.registerParcel(sender, receiver)) {
                clearFields(senderField, receiverField);
                refreshTable();
            }
        } else {
            // Logic path: Updating an existing record
            selectedParcelForEdit.setSender(sender);
            selectedParcelForEdit.setReceiver(receiver);

            // Assuming your manager layer includes an update query matching this signature
            // parcelManager.updateParcel(selectedParcelForEdit);

            selectedParcelForEdit = null;
            clearFields(senderField, receiverField);
            refreshTable();
        }
    }

    private void loadParcelIntoForm(Parcel parcel) {
        this.selectedParcelForEdit = parcel;
        senderField.setText(parcel.getSender());
        receiverField.setText(parcel.getReceiver());
    }

    private void processParcelDeletion(Parcel parcel) {
        // Confirmation validation step
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Delete item " + parcel.getTrackingId() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Assuming your manager layer has a delete operation matching this signature:
                // parcelManager.deleteParcel(parcel.getTrackingId());
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        populateTable(parcelTable, parcelManager.getAllParcels());
    }
}