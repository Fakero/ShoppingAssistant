package com.fakero.shoppingassistant;

/**
 * Created by fakero on 5.8.2017.
 */

public class ShoppingListItem {

    private String name;
    private double price;
    private int amount;
    private Boolean checked;

    public ShoppingListItem() {
        checked = false;
        price = 0.0;
        amount = 1;
    }

    public ShoppingListItem(String name, double price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        checked = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getChecked() {
        return checked;
    }

    public double getTotalPrice() {
        return price * amount;
    }

    @Override
    public String toString() {
        return name;
    }
}
