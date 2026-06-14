package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.model.Parcel;
import com.example.smartlogisticssystem.MyD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ParcelManager {

    public ParcelManager() {
    }

    public boolean registerParcel(String sender, String receiver) {
        if (isInvalidInput(sender) || isInvalidInput(receiver)) {
            return false;
        }

        String trackingId = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String initialStatus = "Manifest Created";

        String sql = "INSERT INTO parcels (tracking_id, sender, receiver, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId);
            pstmt.setString(2, sender.trim());
            pstmt.setString(3, receiver.trim());
            pstmt.setString(4, initialStatus);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting parcel: " + e.getMessage());
            return false;
        }
    }

    public List<Parcel> getAllParcels() {
        List<Parcel> parcels = new ArrayList<>();
        String sql = "SELECT * FROM parcels";

        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Parcel parcel = new Parcel(
                        rs.getString("tracking_id"),
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("status")
                );
                parcels.add(parcel);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all parcels: " + e.getMessage());
        }
        return parcels;
    }

    public boolean updateParcelDetails(String trackingId, String newSender, String newReceiver, String newStatus) {
        if (isInvalidInput(trackingId) || isInvalidInput(newSender) || isInvalidInput(newReceiver) || isInvalidInput(newStatus)) {
            return false;
        }

        String sql = "UPDATE parcels SET sender = ?, receiver = ?, status = ? WHERE tracking_id = ?";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newSender.trim());
            pstmt.setString(2, newReceiver.trim());
            pstmt.setString(3, newStatus.trim());
            pstmt.setString(4, trackingId.trim());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating complete parcel records: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatus(String trackingId, String newStatus) {
        if (isInvalidInput(trackingId) || isInvalidInput(newStatus)) {
            return false;
        }

        String sql = "UPDATE parcels SET status = ? WHERE tracking_id = ?";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus.trim());
            pstmt.setString(2, trackingId.trim());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteParcel(String trackingId) {
        if (isInvalidInput(trackingId)) return false;

        String sql = "DELETE FROM parcels WHERE tracking_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting parcel record: " + e.getMessage());
            return false;
        }
    }

    public List<Parcel> searchParcels(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllParcels();
        }

        String lowerKeyword = keyword.toLowerCase().trim();

        return getAllParcels().stream()
                .filter(p -> p.getTrackingId().toLowerCase().contains(lowerKeyword) ||
                        p.getSender().toLowerCase().contains(lowerKeyword) ||
                        p.getReceiver().toLowerCase().contains(lowerKeyword) ||
                        p.getStatus().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    private boolean isInvalidInput(String input) {
        return input == null || input.trim().isEmpty();
    }
}