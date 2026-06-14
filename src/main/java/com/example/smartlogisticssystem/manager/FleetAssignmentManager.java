package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.FleetAssignment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FleetAssignmentManager {

    public boolean assignFleet(int vehicleId, int driverId, String status) {
        String sql = "INSERT INTO fleet_assignments (vehicle_id, driver_id, status, assignment_date) VALUES (?, ?, ?, NOW())"
                + " ON DUPLICATE KEY UPDATE status = ?, assignment_date = NOW()";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, driverId);
            pstmt.setString(3, status);
            pstmt.setString(4, status);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAssignment(int id, int vehicleId, int driverId, String status) {
        String sql = "UPDATE fleet_assignments SET vehicle_id = ?, driver_id = ?, status = ?, assignment_date = NOW() WHERE id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, driverId);
            pstmt.setString(3, status);
            pstmt.setInt(4, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAssignmentStatus(int assignmentId, String status) {
        String sql = "UPDATE fleet_assignments SET status = ? WHERE id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, assignmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAssignment(int id) {
        String sql = "DELETE FROM fleet_assignments WHERE id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FleetAssignment> getAllAssignments() {
        List<FleetAssignment> list = new ArrayList<>();
        String sql = "SELECT * FROM fleet_assignments";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new FleetAssignment(
                        rs.getInt("id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<FleetAssignment> searchAssignments(String keyword) {
        List<FleetAssignment> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllAssignments();

        String sql = "SELECT * FROM fleet_assignments WHERE status LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new FleetAssignment(
                            rs.getInt("id"),
                            rs.getInt("vehicle_id"),
                            rs.getInt("driver_id"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}