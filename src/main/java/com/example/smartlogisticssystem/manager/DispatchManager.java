package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.Dispatch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispatchManager {

    public boolean createDispatch(int vehicleId, int driverId, int originId, int destId, Timestamp departure, Timestamp estArrival) {
        String sql = "INSERT INTO dispatches (vehicle_id, driver_id, origin_location_id, destination_location_id, departure_time, estimated_arrival, dispatch_status) VALUES (?, ?, ?, ?, ?, ?, 'Scheduled')";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, driverId);
            pstmt.setInt(3, originId);
            pstmt.setInt(4, destId);
            pstmt.setTimestamp(5, departure);
            pstmt.setTimestamp(6, estArrival);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateDispatch(int dispatchId, int vehicleId, int driverId, int originId, int destId, Timestamp departure, Timestamp estArrival, String status) {
        String sql = "UPDATE dispatches SET vehicle_id = ?, driver_id = ?, origin_location_id = ?, destination_location_id = ?, departure_time = ?, estimated_arrival = ?, dispatch_status = ? WHERE dispatch_id = ?";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            pstmt.setInt(2, driverId);
            pstmt.setInt(3, originId);
            pstmt.setInt(4, destId);
            pstmt.setTimestamp(5, departure);
            pstmt.setTimestamp(6, estArrival);
            pstmt.setString(7, status);
            pstmt.setInt(8, dispatchId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDispatch(int dispatchId) {
        String sql = "DELETE FROM dispatches WHERE dispatch_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dispatchId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Dispatch> getAllDispatches() {
        List<Dispatch> list = new ArrayList<>();
        String sql = "SELECT * FROM dispatches";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Dispatch(
                        rs.getInt("dispatch_id"),
                        rs.getInt("vehicle_id"),
                        rs.getInt("driver_id"),
                        rs.getInt("origin_location_id"),
                        rs.getInt("destination_location_id"),
                        rs.getTimestamp("departure_time"),
                        rs.getTimestamp("estimated_arrival"),
                        rs.getTimestamp("actual_arrival"), // FIXED: Added missing field
                        rs.getString("dispatch_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Dispatch> searchDispatches(String keyword) {
        List<Dispatch> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllDispatches();

        String sql = "SELECT * FROM dispatches WHERE dispatch_status LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword.trim() + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Dispatch(
                            rs.getInt("dispatch_id"),
                            rs.getInt("vehicle_id"),
                            rs.getInt("driver_id"),
                            rs.getInt("origin_location_id"),
                            rs.getInt("destination_location_id"),
                            rs.getTimestamp("departure_time"),
                            rs.getTimestamp("estimated_arrival"),
                            rs.getTimestamp("actual_arrival"), // FIXED: Added missing field
                            rs.getString("dispatch_status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean logTransitStatus(String trackingId, int locationId, String status, String remarks) {
        String updateParcelSql = "UPDATE parcels SET status = ? WHERE tracking_id = ?";
        String insertLogSql = "INSERT INTO transit_logs (tracking_id, current_location_id, status, remarks) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyD.getConn()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(updateParcelSql);
                 PreparedStatement p2 = conn.prepareStatement(insertLogSql)) {

                p1.setString(1, status);
                p1.setString(2, trackingId);
                p1.executeUpdate();

                p2.setString(1, trackingId);
                p2.setInt(2, locationId);
                p2.setString(3, status);
                p2.setString(4, remarks);
                p2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}