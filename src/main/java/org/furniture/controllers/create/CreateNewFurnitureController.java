package org.furniture.controllers.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.furniture.services.DBConnect;

import java.sql.SQLException;

public class CreateNewFurnitureController {
    @FXML
    TextField furnitureNameTextField;
    @FXML
    TextField costTextField;
    @FXML
    Button createBtn;
    @FXML
    Button cancelBtn;

    @FXML
    public void initialize() throws SQLException {
        clear();
    }

    public void handleCreate(){
        String furnitureName = furnitureNameTextField.getText();
        int furnitureCost = Integer.parseInt(costTextField.getText());
        DBConnect.queryUpdate("INSERT INTO furniture(name,cost)\n" +
                "VALUES ('" + furnitureName + "','" + furnitureCost + "')");
    }

    private void clear(){
        furnitureNameTextField.clear();
        costTextField.clear();
    }
    public void handleCancel(ActionEvent actionEvent) {
    }
}
