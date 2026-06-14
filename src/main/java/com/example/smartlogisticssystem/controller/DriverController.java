package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.DriverManager;
import com.example.smartlogisticssystem.model.Driver;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DriverController extends BaseController {

    @FXML private TextField driverNameField;
    @FXML private TextField driverPhoneField;
    @FXML private TableView<Driver> driverTable;
    @FXML private TableColumn<Driver, Integer> colId;
    @FXML private TableColumn<Driver, String> colName;
    @FXML private TableColumn<Driver, String> colPhone;

    private final TableColumn<Driver, Void> colActions = new TableColumn<>("Actions");
    private final DriverManager driverManager = new DriverManager();
    private Driver selectedDriverForEdit = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        colActions.setPrefWidth(130);
        driverTable.getColumns().add(colActions);

        addActionsToTable(colActions,
                driver -> {
                    this.selectedDriverForEdit = driver;
                    driverNameField.setText(driver.getName());
                    driverPhoneField.setText(driver.getPhone());
                },
                driver -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "De-register driver " + driver.getName() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // driverManager.deleteDriver(driver.getDriverId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleAddDriver() {
        String name = driverNameField.getText();
        String phone = driverPhoneField.getText();

        if (driverManager.addDriver(name, phone)) {
            clearFields(driverNameField, driverPhoneField);
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", null, "Driver information could not be registered.");
        }
    }

    private void refreshTable() {
        populateTable(driverTable, driverManager.getAllDrivers());
    }
}