package com.soo.learn.java;

/**
 * Created by SongYuHai on 2016/12/14.
 */

public class Movie {
    public static final int FILM_NEW=0;
    public static final int FILM_ADULT=1;
    public static final int FILM_CHILDREN=2;
    private String title;
    private int priceCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(int priceCode) {
        this.priceCode = priceCode;
    }
}
