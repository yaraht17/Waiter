package com.buoistudio.model;


public class DishItem {
    private String id;
    private String avatar;
    private String name;
    private double price;
    private int qty;

    private boolean status;

    public DishItem(String avatar, String name, double price, int qty, boolean status) {
        this.avatar = avatar;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.status = status;
    }

    public DishItem(String avatar, String name, double price, int qty) {
        this.avatar = avatar;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public DishItem(String id, String avatar, String name, double price, int qty) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public DishItem(String id, String avatar, String name, double price, int qty, boolean status) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
