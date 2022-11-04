package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NewOrdersController extends AbstractPageOrderController {
    @FXML private TextField searchTextField;
    @FXML private ListView<String> ordersListView;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button createButton;

    @Override
    protected void initialize() {
        System.out.println("Hello");
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
}
