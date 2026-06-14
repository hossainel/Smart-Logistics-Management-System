package com.example.smartlogisticssystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    // Keeping a static reference allows controllers to change the primary window stage easily
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginView();
    }

    /**
     * Standard method to boot up the Login interface window.
     */
    public static void showLoginView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            primaryStage.setTitle("Smart Logistics System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Keeps login window crisp and un-stretchable
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load Login view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Call this method from your LoginController upon a successful database authentication match.
     * It transforms the window stage into your main administrative layout dashboard.
     */
    public static void changeToDashboardView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Dashboard.fxml"));
            // Dashboards look much better with a spacious 1050x700 resolution
            Scene scene = new Scene(fxmlLoader.load(), 1050, 700);

            primaryStage.setTitle("Smart Logistics System - Admin Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Allows admins to maximize the app workspace
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load Dashboard view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
