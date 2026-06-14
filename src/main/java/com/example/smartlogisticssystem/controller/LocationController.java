package com.example.smartlogisticssystem.controller;

import com.example.smartlogisticssystem.manager.LocationManager;
import com.example.smartlogisticssystem.model.Location;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LocationController extends BaseController {

    @FXML private TextField locationNameField;
    @FXML private TextField cityField;
    @FXML private TextField stateField;
    @FXML private ComboBox<String> typeCombo;

    @FXML private TableView<Location> locationTable;
    @FXML private TableColumn<Location, Integer> colLocationId;
    @FXML private TableColumn<Location, String> colLocationName;
    @FXML private TableColumn<Location, String> colCity;
    @FXML private TableColumn<Location, String> colState;
    @FXML private TableColumn<Location, String> colType;

    private final TableColumn<Location, Void> colActions = new TableColumn<>("Actions");
    private final LocationManager locationManager = new LocationManager();
    private Location selectedLocationForEdit = null;

    @FXML
    public void initialize() {
        typeCombo.setItems(FXCollections.observableArrayList("Hub", "Warehouse", "Sorting Center", "Drop-off Point"));

        colLocationId.setCellValueFactory(new PropertyValueFactory<>("locationId"));
        colLocationName.setCellValueFactory(new PropertyValueFactory<>("locationName"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colState.setCellValueFactory(new PropertyValueFactory<>("stateRegion"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        colActions.setPrefWidth(130);
        locationTable.getColumns().add(colActions);

        addActionsToTable(colActions,
                location -> {
                    this.selectedLocationForEdit = location;
                    locationNameField.setText(location.getLocationName());
                    cityField.setText(location.getCity());
                    stateField.setText(location.getStateRegion());
                    typeCombo.setValue(location.getType());
                },
                location -> {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Remove network node " + location.getLocationName() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            // locationManager.deleteLocation(location.getLocationId());
                            refreshTable();
                        }
                    });
                }
        );

        refreshTable();
    }

    @FXML
    private void handleRegisterLocation() {
        String name = locationNameField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String type = typeCombo.getValue();

        if (locationManager.addLocation(name, city, state, type)) {
            clearFields(locationNameField, cityField, stateField);
            typeCombo.setValue(null);
            refreshTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Infrastructure Error", null,
                    "Failed to deploy station node. Name, City, and Type classification are required.");
        }
    }

    private void refreshTable() {
        populateTable(locationTable, locationManager.getAllLocations());
    }
}