module org.furniture {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;

    opens org.furniture to javafx.fxml;
    exports org.furniture;
    exports org.furniture.enums;

    opens org.furniture.controllers to javafx.fxml;
    exports org.furniture.controllers;

}
