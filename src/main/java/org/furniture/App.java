package org.furniture;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import org.furniture.enums.Page;
import org.furniture.services.DBConnect;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static final String MAIN_MENU = Page.NEW_ORDER_PAGE.toString();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(MAIN_MENU), 1366, 768);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws SQLException {
        DBConnect.loadDriver();
        launch();
        DBConnect.closeDriver();
    }

}
