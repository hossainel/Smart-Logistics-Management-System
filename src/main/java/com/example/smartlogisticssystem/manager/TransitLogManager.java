package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.TransitLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransitLogManager {

    public boolean addTransitLog(String trackingId, int locationId, String status, String remarks) {
        if (trackingId == null || trackingId.trim().isEmpty()) return false;

        String sql = "INSERT INTO transit_logs (tracking_id, current_location_id, status, remarks, timestamp) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId.trim());
            pstmt.setInt(2, locationId);
            pstmt.setString(3, status != null ? status.trim() : null);
            pstmt.setString(4, remarks != null ? remarks.trim() : null);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTransitLog(int logId, String trackingId, int locationId, String status, String remarks) {
        String sql = "UPDATE transit_logs SET tracking_id = ?, current_location_id = ?, status = ?, remarks = ? WHERE log_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId);
            pstmt.setInt(2, locationId);
            pstmt.setString(3, status);
            pstmt.setString(4, remarks);
            pstmt.setInt(5, logId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTransitLog(int logId) {
        String sql = "DELETE FROM transit_logs WHERE log_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, logId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TransitLog> getAllTransitLogs() {
        List<TransitLog> list = new ArrayList<>();
        String sql = "SELECT * FROM transit_logs ORDER BY timestamp DESC";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new TransitLog(
                        rs.getInt("log_id"),
                        rs.getString("tracking_id"),
                        rs.getInt("current_location_id"),
                        rs.getString("status"),
                        rs.getTimestamp("timestamp"),
                        rs.getString("remarks")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TransitLog> getAuditTrailForParcel(String trackingId) {
        List<TransitLog> trailList = new ArrayList<>();
        if (trackingId == null || trackingId.trim().isEmpty()) {
            return trailList;
        }

        String sql = "SELECT * FROM transit_logs WHERE tracking_id = ? ORDER BY timestamp ASC";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trailList.add(new TransitLog(
                            rs.getInt("log_id"),
                            rs.getString("tracking_id"),
                            rs.getInt("current_location_id"),
                            rs.getString("status"),
                            rs.getTimestamp("timestamp"),
                            rs.getString("remarks")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error pulling historical shipment log details: " + e.getMessage());
            e.printStackTrace();
        }
        return trailList;
    }

    public List<TransitLog> searchTransitLogs(String keyword) {
        List<TransitLog> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllTransitLogs();

        String sql = "SELECT * FROM transit_logs WHERE tracking_id LIKE ? OR status LIKE ? OR remarks LIKE ? ORDER BY timestamp DESC";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);
            pstmt.setString(3, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new TransitLog(
                            rs.getInt("log_id"),
                            rs.getString("tracking_id"),
                            rs.getInt("current_location_id"),
                            rs.getString("status"),
                            rs.getTimestamp("timestamp"),
                            rs.getString("remarks")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean clearTransitHistory(String trackingId) {
        if (trackingId == null || trackingId.trim().isEmpty()) return false;

        String sql = "DELETE FROM transit_logs WHERE tracking_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cleaning tracking timeline records: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}