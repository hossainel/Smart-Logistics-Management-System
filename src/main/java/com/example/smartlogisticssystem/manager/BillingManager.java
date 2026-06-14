package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.ShipmentBilling;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingManager {

    public boolean createInvoice(String trackingId, int customerId, double baseFare, double tax, double codAmount, String method) {
        double total = baseFare + tax;
        String sql = "INSERT INTO shipment_billings (tracking_id, customer_id, base_fare, tax_fees, cod_amount, total_amount, payment_status, payment_method) VALUES (?, ?, ?, ?, ?, ?, 'Pending', ?)";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId);
            pstmt.setInt(2, customerId);
            pstmt.setDouble(3, baseFare);
            pstmt.setDouble(4, tax);
            pstmt.setDouble(5, codAmount);
            pstmt.setDouble(6, total);
            pstmt.setString(7, method);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInvoice(int invoiceId, String trackingId, int customerId, double baseFare, double tax, double codAmount, String status, String method) {
        double total = baseFare + tax;
        String sql = "UPDATE shipment_billings SET tracking_id = ?, customer_id = ?, base_fare = ?, tax_fees = ?, cod_amount = ?, total_amount = ?, payment_status = ?, payment_method = ? WHERE invoice_id = ?";

        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, trackingId);
            pstmt.setInt(2, customerId);
            pstmt.setDouble(3, baseFare);
            pstmt.setDouble(4, tax);
            pstmt.setDouble(5, codAmount);
            pstmt.setDouble(6, total);
            pstmt.setString(7, status);
            pstmt.setString(8, method);
            pstmt.setInt(9, invoiceId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePaymentStatus(int invoiceId, String status) {
        String sql = "UPDATE shipment_billings SET payment_status = ? WHERE invoice_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, invoiceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM shipment_billings WHERE invoice_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, invoiceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ShipmentBilling> getAllBillings() {
        List<ShipmentBilling> list = new ArrayList<>();
        String sql = "SELECT * FROM shipment_billings";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ShipmentBilling(
                        rs.getInt("invoice_id"),
                        rs.getString("tracking_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("base_fare"),
                        rs.getDouble("tax_fees"),
                        rs.getDouble("cod_amount"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_status"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("billing_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ShipmentBilling> searchBillings(String keyword) {
        List<ShipmentBilling> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllBillings();

        String sql = "SELECT * FROM shipment_billings WHERE tracking_id LIKE ? OR payment_status LIKE ? OR payment_method LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);
            pstmt.setString(3, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ShipmentBilling(
                            rs.getInt("invoice_id"),
                            rs.getString("tracking_id"),
                            rs.getInt("customer_id"),
                            rs.getDouble("base_fare"),
                            rs.getDouble("tax_fees"),
                            rs.getDouble("cod_amount"),
                            rs.getDouble("total_amount"),
                            rs.getString("payment_status"),
                            rs.getString("payment_method"),
                            rs.getTimestamp("billing_date")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
