package com.example.smartlogisticssystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyD {
    private static final String URL = "jdbc:mysql://localhost:3306/logistics";
    private static final String U = "root";
    private static final String P = ""; // "143justkidding";
    private static Connection conn = null;

    public static Connection getConn() {
        try {
            // Fix: Check if the connection is null OR if it has become closed/invalid
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, U, P);
            }
        } catch (SQLException e) {
            System.err.println("Database Connection Failure: " + e.getMessage());
            e.printStackTrace(); // Keep this during development to see exactly why it fails
        }
        return conn;
    }
}
