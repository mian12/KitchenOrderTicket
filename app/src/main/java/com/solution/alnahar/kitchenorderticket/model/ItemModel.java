package com.solution.alnahar.kitchenorderticket.model;

/**
 * Created by Yasir on 5/17/2017.
 */

public class ItemModel {


    private String itemid;


    private String name;
private  String price;
    private String uom;


    private int quantity;

    private int amount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}