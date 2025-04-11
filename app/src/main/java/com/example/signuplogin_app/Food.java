package com.example.signuplogin_app;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String namefood;
    private int pricefood;
    private String imageUrl;

    public Food() {} // constructor rỗng bắt buộc cho Firebase

    public Food(String id,String namefood, int pricefood, String imageUrl) {
        this.id = id;
        this.namefood = namefood;
        this.pricefood = pricefood;
        this.imageUrl = imageUrl;
    }
    public String getId() {
        return id;
    }
    public String getNamefood() {
        return namefood;
    }

    public int getPricefood() {
        return pricefood;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNamefood(String namefood) {
        this.namefood = namefood;
    }

    public void setPricefood(int pricefood) {
        this.pricefood = pricefood;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

