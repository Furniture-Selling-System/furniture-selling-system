package org.furniture.enums;

public enum Page {
    NEW_ORDER_PAGE      ("/org/furniture/new-orders"),
    CONSTRUCT_PAGE      ("/org/furniture/constructing"),
    SHIP_PAGE           ("/org/furniture/shipping"),
    CONFIRM_PAGE        ("/org/furniture/confirming"),

    ORDER_HISTORY_PAGE  ("/org/furniture/order-history"),

    CUSTOMERS_PAGE      ("/org/furniture/customers"),
    FURNITURE_PAGE      ("/org/furniture/furniture"),
    MATERIALS_PAGE      ("/org/furniture/materials");

    private String fxml;

    Page(String fxml) {
        this.fxml = fxml;
    }

    @Override
    public String toString() {
        return fxml;
    }
}