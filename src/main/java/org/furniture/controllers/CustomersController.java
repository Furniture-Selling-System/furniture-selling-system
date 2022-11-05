package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.furniture.services.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomersController extends AbstractPageController {

    @FXML
    TextField idTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField phoneTextField;
    @FXML
    TextField searchTextField;

    @FXML
    TextArea addressTextArea;

    @FXML
    ListView<String> customerListView;


    @FXML
    Button confirmButton;

    private ArrayList<String> customersArrayList;

    @Override
    protected void initialize() {
        customersArrayList = DBConnect.getCustomers();
        showCustomerListView();
        clearSelectedCustomer();
        handleSelectedCustomerListView();
    }

    @FXML
    private void searchTextFieldOnAction(KeyEvent e) {
        customerListView.getItems().clear();
        customersArrayList = DBConnect.getCustomersByName(searchTextField.getText());
        customerListView.getItems().addAll(customersArrayList);
        customerListView.refresh();
        if(searchTextField.getText().isEmpty())
            initialize();
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent e) {
        String customerID = idTextField.getText();
        String customerName = nameTextField.getText();
        String customerAddress = addressTextArea.getText();
        String customerPhone = phoneTextField.getText();
        if (confirmButton.getText().equals("EDIT")) {
            DBConnect.queryUpdate("UPDATE customer\n" +
                    "SET name='" + customerName + "',address='" + customerAddress + "',phone='" + customerPhone + "'\n" +
                    "WHERE id=" + customerID);
        } else {
            DBConnect.queryUpdate("INSERT INTO customer(name,address,phone)\n" +
                    "VALUES ('" + customerName + "','" + customerAddress + "','" + customerPhone + "')");
        }
        clearSelectedCustomer();
        showCustomerListView();
    }

    private void showCustomerListView() {
        customerListView.getItems().clear();
        customersArrayList = DBConnect.getCustomers();
        customerListView.getItems().addAll(customersArrayList);
        customerListView.refresh();
    }

    private void handleSelectedCustomerListView() {
        confirmButton.setText("EDIT");
        customerListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedCustomer(newValue));
    }

    private void showSelectedCustomer(String customer) {
        ResultSet rs = null;
        try {
            rs = DBConnect.query("SELECT c.id,c.name,c.address,c.phone FROM customer c\n" +
                    "WHERE id=" + customer.charAt(0));
            rs.next();
            idTextField.setText(rs.getString("id"));
            nameTextField.setText(rs.getString("name"));
            phoneTextField.setText(rs.getString("phone"));
            addressTextArea.setText(rs.getString("address"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearSelectedCustomer() {
        idTextField.clear();
        nameTextField.clear();
        phoneTextField.clear();
        addressTextArea.clear();
    }

}
