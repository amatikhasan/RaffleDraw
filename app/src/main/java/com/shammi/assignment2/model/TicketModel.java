package com.shammi.assignment2.model;

public class TicketModel {

    private int id;
    private int number;
    private int raffleID;
    private String raffleTitle;
    private double price;
    private String purchasedAt;
    private int customerId;
    private String customerName;
    private String customerContact;

    public TicketModel(int id, int number, int raffleID,String raffleTitle, double price, String purchasedAt, int customerId, String customerName, String customerContact) {
        this.id = id;
        this.number = number;
        this.raffleID = raffleID;
        this.raffleTitle = raffleTitle;
        this.price = price;
        this.purchasedAt = purchasedAt;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerContact = customerContact;
    }


    public TicketModel(int number,int raffleID,String raffleTitle, double price, String purchasedAt, int customerId) {
        this.number = number;
        this.raffleID = raffleID;
        this.raffleTitle = raffleTitle;
        this.price = price;
        this.purchasedAt = purchasedAt;
        this.customerId = customerId;

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getRaffleTitle() {
        return raffleTitle;
    }

    public void setRaffleTitle(String raffleTitle) {
        this.raffleTitle = raffleTitle;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRaffleID() {
        return raffleID;
    }

    public void setRaffleID(int raffleID) {
        this.raffleID = raffleID;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(String purchasedAt) {
        this.purchasedAt = purchasedAt;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


}
