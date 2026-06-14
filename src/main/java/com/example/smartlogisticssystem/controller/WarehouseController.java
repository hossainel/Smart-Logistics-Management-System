package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.WarehouseManager;
import com.example.smartlogisticssystem.model.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class WarehouseController extends BaseController {

    @FXML private TextField warehouseNameField;
    @FXML private TextField locationField;
    @FXML private TableView<Warehouse> warehouseTable;
    @FXML private TableColumn<Warehouse, Integer> colId;
    @FXML private TableColumn<Warehouse, String> colName;
    @FXML private TableColumn<Warehouse, String> colLocation;


    // Create an un-mapped UI column explicitly for inline action buttons
    private final TableColumn<Warehouse, Void> colActions = new TableColumn<>("Actions");
    private final WarehouseManager warehouseManager = new WarehouseManager();
    private Warehouse selectedWarehouseForEdit = null;

    @FXML
    public void initialize() {
        // 1. Map data keys to structural properties
        colId.setCellValueFactory(new PropertyValueFactory<>("warehouseId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("warehouseName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        // 2. Configure actions layout properties and mount dynamically
        colActions.setPrefWidth(130);
        warehouseTable.getColumns().add(colActions);

        // 3. Register functional callbacks via abstract BaseController definitions
        addActionsToTable(colActions,
                warehouse -> { // On Edit Action click
                    this.selectedWarehouseForEdit = warehouse;
                    warehouseNameField.setText(warehouse.getWarehouseName());
                    locationField.setText(warehouse.getLocation());
                },
                warehouse -> { // On Delete Action click
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Remove " + warehouse.getWarehouseName() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // warehouseManager.deleteWarehouse(warehouse.getWarehouseId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void addWarehouse() {
        String name = warehouseNameField.getText();
        String loc = locationField.getText();

        if (warehouseManager.registerWarehouse(name, loc)) {
            clearFields(warehouseNameField, locationField);
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Failed to register warehouse hub. Check inputs.");
        }
    }

    private void refreshTable() {
        populateTable(warehouseTable, warehouseManager.getAllWarehouses());
    }
}