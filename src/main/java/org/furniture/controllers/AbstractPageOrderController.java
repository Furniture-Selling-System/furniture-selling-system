package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public abstract class AbstractPageOrderController extends AbstractPageController {
    @FXML protected TextField searchTextField;
    @FXML protected Button confirmButton;

    @FXML
    protected void searchTextFieldOnAction(KeyEvent e) {
        // TODO
    }

    @FXML
    abstract protected void confirmButtonOnAction(ActionEvent e);
}
