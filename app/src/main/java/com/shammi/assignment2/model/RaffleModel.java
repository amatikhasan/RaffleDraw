package com.shammi.assignment2.model;

public class RaffleModel {

    private int id;
    private String title;
    private String desc;
    private byte[] image;
    private String type;
    private int ticketPrice;
    private int ticketSold;
    private int ticketLimit;
    private int winnerLimit;
    private String start;
    private String draw;

    public RaffleModel(String title, String desc, byte[] image, String type, int ticketPrice, int ticketLimit, int winnerLimit, String start, String draw) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.type = type;
        this.ticketPrice = ticketPrice;
        this.ticketLimit = ticketLimit;
        this.winnerLimit = winnerLimit;
        this.start = start;
        this.draw = draw;
    }

    public RaffleModel(int id, String title, String desc, byte[] image, String type, int ticketPrice,int ticketSold, int ticketLimit, int winnerLimit, String start, String draw) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.type = type;
        this.ticketPrice = ticketPrice;
        this.ticketSold = ticketSold;
        this.ticketLimit = ticketLimit;
        this.winnerLimit = winnerLimit;
        this.start = start;
        this.draw = draw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getTicketSold() {
        return ticketSold;
    }

    public void setTicketSold(int ticketSold) {
        this.ticketSold = ticketSold;
    }


    public int getTicketLimit() {
        return ticketLimit;
    }

    public void setTicketLimit(int ticketLimit) {
        this.ticketLimit = ticketLimit;
    }

    public int getWinnerLimit() {
        return winnerLimit;
    }

    public void setWinnerLimit(int winnerLimit) {
        this.winnerLimit = winnerLimit;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }








}
