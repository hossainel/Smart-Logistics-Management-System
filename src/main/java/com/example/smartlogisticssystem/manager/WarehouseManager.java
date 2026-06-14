package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.Warehouse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseManager {

    public boolean registerWarehouse(String warehouseName, String location) {
        if (warehouseName == null || location == null || warehouseName.trim().isEmpty()) return false;

        String sql = "INSERT INTO warehouses (warehouse_name, location) VALUES (?, ?)";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, warehouseName.trim());
            pstmt.setString(2, location.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateWarehouse(int warehouseId, String warehouseName, String location) {
        if (warehouseName == null || location == null || warehouseName.trim().isEmpty()) return false;

        String sql = "UPDATE warehouses SET warehouse_name = ?, location = ? WHERE warehouse_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, warehouseName.trim());
            pstmt.setString(2, location.trim());
            pstmt.setInt(3, warehouseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWarehouse(int warehouseId) {
        String sql = "DELETE FROM warehouses WHERE warehouse_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, warehouseId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM warehouses";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Warehouse(
                        rs.getInt("warehouse_id"),
                        rs.getString("warehouse_name"),
                        rs.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Warehouse> searchWarehouses(String keyword) {
        List<Warehouse> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllWarehouses();

        String sql = "SELECT * FROM warehouses WHERE warehouse_name LIKE ? OR location LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Warehouse(
                            rs.getInt("warehouse_id"),
                            rs.getString("warehouse_name"),
                            rs.getString("location")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
