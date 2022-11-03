package org.furniture.enums;

public enum OrderStatus {
    WAITING         (0),
    CONSTURCTING    (1),
    SHIPPING        (2),
    CONFIRMING      (3),
    CANCELLED       (8),
    DONE            (9);

    private int status;

    OrderStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
