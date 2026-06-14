package com.example.smartlogisticssystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BaseController {

    protected void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void clearFields(TextField... fields) {
        for (TextField field : fields) {
            if (field != null) field.clear();
        }
    }

    protected <T> void populateTable(TableView<T> table, List<T> dataList) {
        if (table != null && dataList != null) {
            table.setItems(FXCollections.observableArrayList(dataList));
        }
    }

    /**
     * Centralized factory to inject Edit and Delete action buttons directly into any TableView column
     */
    protected <T> void addActionsToTable(TableColumn<T, Void> actionCol, Consumer<T> onEdit, Consumer<T> onDelete) {
        Callback<TableColumn<T, Void>, TableCell<T, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");
                    private final HBox container = new HBox(8, editBtn, deleteBtn);

                    {
                        editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");

                        editBtn.setOnAction(event -> {
                            T data = getTableView().getItems().get(getIndex());
                            onEdit.accept(data);
                        });

                        deleteBtn.setOnAction(event -> {
                            T data = getTableView().getItems().get(getIndex());
                            onDelete.accept(data);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        };
        actionCol.setCellFactory(cellFactory);
    }

    /**
     * Centralized Refresh Utility: Re-fetches fresh data from the database layer
     * and safely populates the target TableView.
     */
    protected <T> void refreshTableData(TableView<T> table, java.util.function.Supplier<List<T>> databaseFetchMethod) {
        if (table != null && databaseFetchMethod != null) {
            List<T> freshData = databaseFetchMethod.get();
            populateTable(table, freshData);
        }
    }

    /**
     * Centralized Live Search Utility: Binds a TextField to a TableView for real-time
     * filtering. As the user types, the table filters instantly without database queries.
     *
     * @param searchField The UI input text box
     * @param table The target TableView component
     * @param baseData The unfiltered, master list of data from the database
     * @param searchLogic The condition rule checking if a row matches the search text
     */
    protected <T> void setupLiveSearch(TextField searchField, TableView<T> table, List<T> baseData, Function<String, Predicate<T>> searchLogic) {
        if (searchField == null || table == null || baseData == null) return;

        // Wrap the master raw data inside an observable dynamic FilteredList
        FilteredList<T> filteredData = new FilteredList<>(FXCollections.observableArrayList(baseData), p -> true);

        // Add a listener to intercept user keyboard typing events
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                // If search field is empty, show all records
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                return searchLogic.apply(lowerCaseFilter).test(item);
            });
        });

        // Re-bind the table's active items to the filtered view wrapper
        table.setItems(filteredData);
    }
}
