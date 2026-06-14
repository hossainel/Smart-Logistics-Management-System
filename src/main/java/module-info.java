module com.example.smartlogisticssystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.smartlogisticssystem to javafx.fxml;
    opens com.example.smartlogisticssystem.controller to javafx.fxml;
    opens com.example.smartlogisticssystem.model to javafx.fxml;
    opens com.example.smartlogisticssystem.manager to javafx.fxml;

    exports com.example.smartlogisticssystem;
    exports com.example.smartlogisticssystem.controller;
    exports com.example.smartlogisticssystem.model;
    exports com.example.smartlogisticssystem.manager;
}