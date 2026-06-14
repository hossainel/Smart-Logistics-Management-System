package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.CustomerManager;
import com.example.smartlogisticssystem.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerController extends BaseController {

    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField customerAddressField;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colAddress;

    private final TableColumn<Customer, Void> colActions = new TableColumn<>("Actions");
    private final CustomerManager customerManager = new CustomerManager();
    private Customer selectedCustomerForEdit = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        colActions.setPrefWidth(130);
        customerTable.getColumns().add(colActions);

        addActionsToTable(colActions,
                customer -> {
                    this.selectedCustomerForEdit = customer;
                    customerNameField.setText(customer.getName());
                    customerPhoneField.setText(customer.getPhone());
                    customerAddressField.setText(customer.getAddress());
                },
                customer -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Purge client profile " + customer.getName() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // customerManager.deleteCustomer(customer.getCustomerId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleRegisterCustomer() {
        String name = customerNameField.getText();
        String phone = customerPhoneField.getText();
        String address = customerAddressField.getText();

        if (customerManager.registerCustomer(name, phone, address)) {
            // Inherited shared utilities from BaseController
            clearFields(customerNameField, customerPhoneField, customerAddressField);
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Validation Error", null,
                    "Account record validation failed. Name cannot be blank.");
        }
    }

    private void refreshTable() {
        // Inherited generic table utility method from BaseController
        populateTable(customerTable, customerManager.getAllCustomers());
    }
}