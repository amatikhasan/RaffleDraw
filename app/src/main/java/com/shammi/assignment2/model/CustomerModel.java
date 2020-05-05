package com.shammi.assignment2.model;

public class CustomerModel {

    private int id;
    private String name;
    private String contact;

    public CustomerModel(int id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public CustomerModel(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
