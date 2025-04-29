package com.CourtControl.model;

public class BookingModel {
    private int bookingId;
    private String customerName;
    private String customerPhone;
    private String courtName;
    private String bookingDate;
    private String timeSlot;
    private String status;
    private String price;

    public BookingModel(int bookingId, String customerName, String customerPhone, String courtName, 
                        String bookingDate, String timeSlot, String status, String price) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.courtName = courtName;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        this.status = status;
        this.price = price;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCourtName() {
        return courtName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }
}