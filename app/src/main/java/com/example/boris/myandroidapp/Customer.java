package com.example.boris.myandroidapp;

/**
 * Created by Boris on 6/2/2018.
 */

public class Customer {
    private String first_name;
    private String last_name;
    private int customer_id;
    private int usual_fee;

    public Customer (String fname, String lname, int id, int fee) {
        this.first_name=fname;
        this.last_name=lname;
        this.customer_id=id;
        this.usual_fee=fee;
    }

    public String getCustomerFullName() {
        return String.format("%s %s", first_name, last_name);
    }

    public int getCustomerId() {
        return customer_id;
    }

    public int getCustomerUsualFee() {
        return usual_fee;
    }
}
