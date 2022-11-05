package org.furniture.models;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.furniture.enums.OrderStatus;

public class Order {
    private String name;
    private double totalPrice;
    private HashMap<Furniture, Integer> furnitures;
    private String address;
    private LocalDateTime creationDateTime;
    private OrderStatus status;

    private Customer customer;

    public Order(String name, double totalPrice, String address, LocalDateTime creationDateTime, OrderStatus status) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.status = status;
    }

    public void addFurniture(Furniture furniture, int quantity) {
        furnitures.put(furniture, quantity);
    }

    public double calculateTotalPrice() {
        totalPrice = 0;
        for (Furniture furniture : furnitures.keySet()) {
            totalPrice += furniture.getPrice() * furnitures.get(furniture);
        }
        return totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Furniture, Integer> getFurnitures() {
        return furnitures;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public String getAddress() {
        return address;
    }

    public Customer getCustomer() {
        return customer;
    }
}
