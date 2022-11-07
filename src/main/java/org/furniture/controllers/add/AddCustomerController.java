package org.furniture.controllers.add;

import java.io.IOException;

import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.OrderStatus;
import org.furniture.enums.Page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import org.furniture.models.Customer;
import org.furniture.services.DBConnect;

public class AddCustomerController extends AbstractPageController {

    @FXML
    TextField idTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    TextArea addressTextArea;

    @Override
    protected void initialize() {
        idTextField.setText(String.valueOf((DBConnect.getCustomerLastID() + 1)));
    }

    public void confirmButtonOnAction(ActionEvent actionEvent) throws IOException {
        if (validation()) {
            Customer customer = new Customer(idTextField.getText(),
                    nameTextField.getText(),
                    addressTextArea.getText(),
                    phoneTextField.getText());
            DBConnect.insertCustomer(customer);

            Alert alert = new Alert(AlertType.INFORMATION, "User has been successfully added.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();

            UIManager.setPage(Page.CUSTOMERS_PAGE);
        }
    }


    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) throws IOException {
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }

    private boolean isInt(String str) {
        return str.matches("\\d+");
    }

    private boolean validation(){
        return true;
    }
}
