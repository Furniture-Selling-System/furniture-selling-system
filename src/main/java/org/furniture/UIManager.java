package org.furniture;

import java.io.IOException;

import org.furniture.enums.Page;

public class UIManager {
    public static void setPage(Page page) throws IOException {
        App.setRoot(page.toString());
    }
}
