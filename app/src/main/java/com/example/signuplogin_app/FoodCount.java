package com.example.signuplogin_app;

public class FoodCount {
    private String name;
    public int count;

    public FoodCount(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}

