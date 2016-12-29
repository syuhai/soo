package com.soo.learn.java;

import java.util.Vector;

/**
 * Created by SongYuHai on 2016/12/14.
 */

public class Customer {
    private String name;
    private Vector<Rental> rentals=new Vector<>();

    public Customer(String name) {
        this.name = name;
    }
    public void addRental(Rental rantal){
        rentals.add(rantal);
    }

    public String getName() {
        return name;
    }
    public String statement;
}
