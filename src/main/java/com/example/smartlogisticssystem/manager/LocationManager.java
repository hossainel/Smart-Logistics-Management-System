package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    public boolean addLocation(String locationName, String city, String stateRegion, String type) {
        if (locationName == null || city == null || type == null || locationName.trim().isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO locations (location_name, city, state_region, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, locationName.trim());
            pstmt.setString(2, city.trim());
            pstmt.setString(3, stateRegion != null ? stateRegion.trim() : null);
            pstmt.setString(4, type.trim());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding infrastructure location node: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateLocation(int locationId, String locationName, String city, String stateRegion, String type) {
        if (locationName == null || city == null || type == null || locationName.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE locations SET location_name = ?, city = ?, state_region = ?, type = ? WHERE location_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, locationName.trim());
            pstmt.setString(2, city.trim());
            pstmt.setString(3, stateRegion != null ? stateRegion.trim() : null);
            pstmt.setString(4, type.trim());
            pstmt.setInt(5, locationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLocation(int locationId) {
        String sql = "DELETE FROM locations WHERE location_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, locationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Location> getAllLocations() {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM locations";

        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Location(
                        rs.getInt("location_id"),
                        rs.getString("location_name"),
                        rs.getString("city"),
                        rs.getString("state_region"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving location records: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Location> searchLocations(String keyword) {
        List<Location> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllLocations();

        String sql = "SELECT * FROM locations WHERE location_name LIKE ? OR city LIKE ? OR state_region LIKE ? OR type LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);
            pstmt.setString(3, match);
            pstmt.setString(4, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Location(
                            rs.getInt("location_id"),
                            rs.getString("location_name"),
                            rs.getString("city"),
                            rs.getString("state_region"),
                            rs.getString("type")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}