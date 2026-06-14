package com.example.smartlogisticssystem.manager;

import com.example.smartlogisticssystem.MyD;
import com.example.smartlogisticssystem.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    public boolean registerCustomer(String name, String phone, String address) {
        if (name == null || phone == null || address == null || name.trim().isEmpty()) return false;

        String sql = "INSERT INTO customers (name, phone, address) VALUES (?, ?, ?)";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            pstmt.setString(2, phone.trim());
            pstmt.setString(3, address.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(int customerId, String name, String phone, String address) {
        if (name == null || phone == null || address == null || name.trim().isEmpty()) return false;

        String sql = "UPDATE customers SET name = ?, phone = ?, address = ? WHERE customer_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name.trim());
            pstmt.setString(2, phone.trim());
            pstmt.setString(3, address.trim());
            pstmt.setInt(4, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = MyD.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> searchCustomers(String keyword) {
        List<Customer> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllCustomers();

        String sql = "SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ? OR address LIKE ?";
        try (Connection conn = MyD.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String match = "%" + keyword.trim() + "%";
            pstmt.setString(1, match);
            pstmt.setString(2, match);
            pstmt.setString(3, match);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("address")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
