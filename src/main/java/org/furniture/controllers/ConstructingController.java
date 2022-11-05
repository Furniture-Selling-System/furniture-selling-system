package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import org.furniture.services.DBConnect;

import java.util.ArrayList;

public class ConstructingController extends AbstractPageOrderController {

    @FXML
    ListView<String> ordersListView;

    private ArrayList<String> ordersArrayList;
    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initialize() {
        ordersArrayList = DBConnect.getSaleOrdersByStatus(1);
        showOrdersListView();
        handleSelectedOrdersListView();
        clearSelectedOrders();
    }

    private void showOrdersListView() {
        ordersListView.getItems().clear();
        ordersArrayList = DBConnect.getSaleOrdersByStatus(1);
        ordersListView.getItems().addAll(ordersArrayList);
        ordersListView.refresh();
    }

    private void handleSelectedOrdersListView() {
        ordersListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedOrder(newValue));
    }

    private void showSelectedOrder(String constructing) {
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

    private void clearSelectedOrders() {
//        idTextField.clear();
//        nameTextField.clear();
//        phoneTextField.clear();
//        addressTextArea.clear();
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
