package org.furniture.models;

import org.furniture.exceptions.InvalidQuantityException;

public class Material {
    private String name;
    private int quantity;

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
}
