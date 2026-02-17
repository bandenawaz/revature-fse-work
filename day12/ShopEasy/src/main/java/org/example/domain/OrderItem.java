package org.example.domain;

public class OrderItem {

    String productName;
    public String category;
    public double price;
    public int quantity;

    public OrderItem(String productName, String category, double price, int quantity) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;

    }
}
