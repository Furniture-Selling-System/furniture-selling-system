package org.furniture.exceptions;

import java.io.IOException;

public class InvalidQuantityException extends IOException {
    public InvalidQuantityException() {
        super();
    }

    public InvalidQuantityException(String message) {
        super(message);
    }
}
