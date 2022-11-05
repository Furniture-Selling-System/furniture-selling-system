package org.furniture.models;

import org.furniture.exceptions.InvalidQuantityException;

public class Material {

    private String id;
    private String name;
    private int quantity;

    private int minimum;

    public Material(String id, String name, int quantity, int minimum) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.minimum = minimum;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws InvalidQuantityException {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity must be a positive integer.");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) throws InvalidQuantityException {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity must be a positive integer.");
        }
        this.quantity += quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", minimum=" + minimum +
                '}';
    }
}
