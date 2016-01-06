package com.buoistudio.model;


public class DishItem {
    private String name;
    private double price;
    private String image;

    public DishItem() {
    }

    public DishItem(String name, double price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
