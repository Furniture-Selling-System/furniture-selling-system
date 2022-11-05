package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.furniture.services.DBConnect;

import java.util.AbstractList;

public class NewOrdersController extends AbstractPageOrderController {
    @FXML private TextField searchTextField;
    @FXML private ListView<String> ordersListView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button createButton;

    private AbstractList<String> ordersArrayList;

    @Override
    protected void initialize() {
        ordersArrayList = DBConnect.getSaleOrdersByStatus(0);
        showOrderListView();
        handleSelectedOrderListView();
        clearSelectedOrder();
    }

    private void showOrderListView() {
        ordersListView.getItems().clear();
        ordersArrayList = DBConnect.getSaleOrdersByStatus(0);
        ordersListView.getItems().addAll(ordersArrayList);
        ordersListView.refresh();
    }

    private void handleSelectedOrderListView() {
        ordersListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedOrder(newValue));
    }

    private void showSelectedOrder(String shipping) {
//        ResultSet rs = null;
//        try {
//            rs = DBConnect.query("SELECT c.id,c.name,c.address,c.phone FROM customer c\n" +
//                    "WHERE id=" + shipping.charAt(0));
//            rs.next();
//            idTextField.setText(rs.getString("id"));
//            nameTextField.setText(rs.getString("name"));
//            phoneTextField.setText(rs.getString("phone"));
//            addressTextArea.setText(rs.getString("address"));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void clearSelectedOrder() {
//        idTextField.clear();
//        nameTextField.clear();
//        phoneTextField.clear();
//        addressTextArea.clear();
    }
    @FXML
    private void ordersListViewOnAction (MouseEvent e) {
        // TODO
    }

    @FXML
    private void cancelButtonOnAction (ActionEvent e) {
        // TODO
    }

    @FXML
    private void createButtonOnAction (ActionEvent e) {
        // TODO
    }

    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        // TODO Auto-generated method stub
    }

    protected void searchTextFieldOnAction(KeyEvent e){
        ordersListView.getItems().clear();
        ordersArrayList = DBConnect.getSaleOrdersByName(searchTextField.getText());
        ordersListView.getItems().addAll(ordersArrayList);
        ordersListView.refresh();
        if(searchTextField.getText().isEmpty())
            initialize();
    }
}
