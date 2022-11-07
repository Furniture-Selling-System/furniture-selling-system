package org.furniture.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

import org.furniture.UIManager;
import org.furniture.enums.Page;
import org.furniture.models.Customer;
import org.furniture.models.Material;
import org.furniture.services.DBConnect;

import java.io.IOException;
import java.util.List;

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
    ListView<Customer> customerListView;


    @FXML
    Button confirmButton;

    private ObservableList<Customer> data;
    private Customer selectingCustomer;

    @Override
    protected void initialize() {
        getData();
        showMaterialListView();
        clearSelectMaterial();
        handleSelectedMaterialListView();
    }

    public void getData() {
        List<Customer> customers = DBConnect.getCustomers();
        data = FXCollections.observableList(customers);
    }

    private void clearSelectMaterial() {
        customerListView.refresh();
        idTextField.clear();
        nameTextField.clear();
        addressTextArea.clear();
        phoneTextField.clear();
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent e){
        selectingCustomer = customerListView.getSelectionModel().getSelectedItem();
        selectingCustomer.setName(nameTextField.getText());
        selectingCustomer.setAddress(addressTextArea.getText());
        selectingCustomer.setPhone(phoneTextField.getText());
        DBConnect.updateCustomer(selectingCustomer);

        Alert alert = new Alert(AlertType.INFORMATION, "Customer has been updated.");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
        initialize();

        clearSelectMaterial();
        showMaterialListView();
    }


    @FXML
    public void addCustomerButtonOnAction(ActionEvent actionEvent) throws IOException {
        UIManager.setPage(Page.CREATE_CUSTOMER_PAGE);
    }

    private void showMaterialListView() {
        customerListView.setItems(data);
    }

    private void handleSelectedMaterialListView() {
        customerListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> customerListView(newValue));
    }

    private void customerListView(Customer customer) {
        idTextField.setText(customer.getId());
        nameTextField.setText(customer.getName());
        addressTextArea.setText(customer.getAddress());
        phoneTextField.setText(customer.getPhone());
    }

    @FXML
    private void searchTextFieldOnAction(KeyEvent e) {
        List<Customer> customers = DBConnect.getCustomersByName(searchTextField.getText());
        data = FXCollections.observableList(customers);
        showMaterialListView();
        if (searchTextField.getText().isEmpty())
            initialize();
    }

}
