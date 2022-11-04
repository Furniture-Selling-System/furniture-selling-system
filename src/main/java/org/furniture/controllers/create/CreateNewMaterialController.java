package org.furniture.controllers.create;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.furniture.services.DBConnect;

public class CreateNewMaterialController {
    @FXML
    TextField materialNameTextField;
    @FXML
    TextField quantityTextField;

    @FXML
    TextField minimumTextField;

    @FXML
    Button createBtn;
    @FXML
    Button cancelBtn;

    @FXML
    public void initialize(){
        clear();
    }

    public void handleCreate(){
        String materialName = materialNameTextField.getText();
        int materialQuantity = Integer.parseInt(minimumTextField.getText());
        int materialMinimum = Integer.parseInt(quantityTextField.getText());
        DBConnect.queryUpdate("INSERT INTO material(name,quantity,minimum)\n" +
                "VALUES ('" + materialName + "','" + materialQuantity + "','" + materialMinimum + "')");
        clear();
    }

    private void clear(){
        materialNameTextField.clear();
        quantityTextField.clear();
        minimumTextField.clear();
    }

    public void handleCancel(){

    }
}
