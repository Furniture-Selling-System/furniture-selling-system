package org.furniture.exceptions;

import java.io.IOException;

public class DuplicateDataException extends IOException {
    public DuplicateDataException() {
        super();
    }

    public DuplicateDataException(String message) {
        super(message);
    }
}
