package org.furniture.models;

import java.util.HashMap;

public class Furniture {
    private String name;
    private double price;
    private HashMap<Material, Integer> materials;

    public Furniture(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public HashMap<Material, Integer> getMaterials() {
        return materials;
    }

    public void addMaterial(Material material, int quantity) {
        materials.put(material, quantity);
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}