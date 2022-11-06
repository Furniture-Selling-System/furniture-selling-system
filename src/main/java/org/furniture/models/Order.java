package org.furniture.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import org.furniture.enums.OrderStatus;

public class Order {

    private String id;
    private String name;
    private int totalPrice;
    private HashMap<Furniture, Integer> furnitures;
    private String address;
    private Date creationDateTime;
    private OrderStatus status;
    private Customer customer;

    public Order(String id, String name, int totalPrice, String address, Date creationDateTime, OrderStatus status, Customer customer) {
        this.id = id;
        this.name = name;
        this.totalPrice = totalPrice;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.status = status;
        this.customer = customer;
        furnitures = new HashMap<>();
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

    public String getId() {
        return id;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Date getCreationDateTime() {
        return creationDateTime;
    }

    public String getAddress() {
        return address;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", totalPrice=" + totalPrice +
                ", furnitures=" + furnitures +
                ", address='" + address + '\'' +
                ", creationDateTime=" + creationDateTime +
                ", status=" + status +
                ", customer=" + customer +
                '}';
    }
}
