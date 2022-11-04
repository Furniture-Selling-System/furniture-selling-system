package org.furniture.enums;

public enum Page {
    ORDER_PAGE          ("order_page"),
    CHECK_MATERIAL_PAGE ("check_material_page"),
    CONSTRUCT_PAGE      ("construct_page"),
    SHIP_PAGE           ("ship_page"),
    CONFIRM_PAGE        ("confirm_page"),

    PRIMARY             ("primary");

    private String fxml;

    Page(String fxml) {
        this.fxml = fxml;
    }

    @Override
    public String toString() {
        return fxml;
    }
}
