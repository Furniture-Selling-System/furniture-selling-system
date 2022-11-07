package org.furniture.models;

import java.util.HashMap;

public class Furniture {

    private String id;
    private String name;
    private double price;
    private HashMap<Material, Integer> materials;


    public Furniture(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.materials = new HashMap<>();
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

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " : " + name;
    }
}