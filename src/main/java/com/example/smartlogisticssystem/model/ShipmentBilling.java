package com.example.smartlogisticssystem.model;

import java.sql.Timestamp;

public class ShipmentBilling {
    private int invoiceId;
    private String trackingId;
    private int customerId;
    private double baseFare;
    private double taxFees;
    private double codAmount;
    private double totalAmount;
    private String paymentStatus; // Pending, Paid, Refunded
    private String paymentMethod; // Cash, Credit Card, Mobile Banking, COD
    private Timestamp billingDate;

    public ShipmentBilling(int invoiceId, String trackingId, int customerId, double baseFare, double taxFees, double codAmount, double totalAmount, String paymentStatus, String paymentMethod, Timestamp billingDate) {
        this.invoiceId = invoiceId;
        this.trackingId = trackingId;
        this.customerId = customerId;
        this.baseFare = baseFare;
        this.taxFees = taxFees;
        this.codAmount = codAmount;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.billingDate = billingDate;
    }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }

    public double getTaxFees() { return taxFees; }
    public void setTaxFees(double taxFees) { this.taxFees = taxFees; }

    public double getCodAmount() { return codAmount; }
    public void setCodAmount(double codAmount) { this.codAmount = codAmount; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Timestamp getBillingDate() { return billingDate; }
    public void setBillingDate(Timestamp billingDate) { this.billingDate = billingDate; }
}