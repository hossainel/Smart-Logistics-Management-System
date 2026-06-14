package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverManager {

    public boolean addDriver(String name, String phone) {
        if (name == null || phone == null || name.trim().isEmpty()) return false;

        String sql = "INSERT INTO drivers (name, phone) VALUES (?, ?)";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            pstmt.setString(2, phone.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDriver(int driverId, String name, String phone) {
        if (name == null || phone == null || name.trim().isEmpty()) return false;

        String sql = "UPDATE drivers SET name = ?, phone = ? WHERE driver_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            pstmt.setString(2, phone.trim());
            pstmt.setInt(3, driverId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDriver(int driverId) {
        String sql = "DELETE FROM drivers WHERE driver_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, driverId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Driver> getAllDrivers() {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM drivers";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Driver(
                        rs.getInt("driver_id"),
                        rs.getString("name"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Driver> searchDrivers(String keyword) {
        List<Driver> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllDrivers();

        String sql = "SELECT * FROM drivers WHERE name LIKE ? OR phone LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Driver(
                            rs.getInt("driver_id"),
                            rs.getString("name"),
                            rs.getString("phone")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
