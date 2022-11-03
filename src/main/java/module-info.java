module org.furniture {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.furniture to javafx.fxml;
    exports org.furniture;
}
