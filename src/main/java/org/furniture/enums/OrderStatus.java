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

    public static OrderStatus findStatus(int status) {
        switch (status) {
            case (0): return WAITING;
            case (1): return CONSTURCTING;
            case (2): return SHIPPING;
            case (3): return CONFIRMING;
            case (8): return CANCELLED;
            case (9): return DONE;
            default: return null;
        }
    }
}
