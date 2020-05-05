package com.shammi.assignment2.model;

public class WinnerModel {
    private int id;

    public WinnerModel(int id, int raffleID, int ticketNumber, int customerID) {
        this.id = id;
        this.raffleID = raffleID;
        this.ticketNumber = ticketNumber;
        this.customerID = customerID;
    }

    public WinnerModel(int raffleID, int ticketNumber) {
        this.raffleID = raffleID;
        this.ticketNumber = ticketNumber;
    }

    private int raffleID;
    private int ticketNumber;
    private int customerID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRaffleID() {
        return raffleID;
    }

    public void setRaffleID(int raffleID) {
        this.raffleID = raffleID;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
