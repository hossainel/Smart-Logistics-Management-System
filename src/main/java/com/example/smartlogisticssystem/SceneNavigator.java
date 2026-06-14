package com.example.smartlogisticssystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;

public class SceneNavigator {

    /**
     * Loads an FXML file directly into the center area of the main dashboard BorderPane.
     * Looks for the FXML in the same directory/package structure as this class.
     */
    public static void loadCenterPanel(BorderPane rootPane, String fxmlFileName) {
        if (rootPane == null) {
            System.err.println("Navigation Error: Root BorderPane is null.");
            return;
        }

        try {
            // Since this class is in the root package, it looks directly in the matching resources root
            URL fxmlUrl = SceneNavigator.class.getResource(fxmlFileName);

            if (fxmlUrl == null) {
                throw new IOException("FXML file not found: " + fxmlFileName);
            }

            // Clear the old panel to help JavaFX free up memory
            rootPane.setCenter(null);

            // Load and display the new panel
            Parent node = FXMLLoader.load(fxmlUrl);
            rootPane.setCenter(node);

        } catch (IOException e) {
            System.err.println("UI Switch Failed: Error loading " + fxmlFileName);
            System.err.println("Navigation Error: " + e.getMessage());
        }
    }
}
