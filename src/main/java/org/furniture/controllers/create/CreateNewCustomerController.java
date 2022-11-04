package org.furniture.controllers.create;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.furniture.services.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateNewCustomerController {

    @FXML
    TextField customerNameTextField;
    @FXML
    TextField phoneTextField;

    @FXML
    TextArea addressTextArea;

    @FXML
    Button createBtn;
    @FXML
    Button cancelBtn;

    @FXML
    public void initialize(){
        clear();
    }

    public void handleCreate(){
        String customerName = customerNameTextField.getText();
        String customerAddress = addressTextArea.getText();
        String customerPhone = phoneTextField.getText();
        System.out.println(customerName);
        DBConnect.queryUpdate("INSERT INTO customer(name,address,phone)\n" +
                "VALUES ('" + customerName + "','" + customerAddress + "','" + customerPhone + "')");
        clear();
    }

    private void clear(){
        customerNameTextField.clear();
        phoneTextField.clear();
        addressTextArea.clear();
    }

    public void handleCancel(){

    }
}
