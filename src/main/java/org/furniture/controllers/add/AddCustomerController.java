package org.furniture.controllers.add;

import java.io.IOException;

import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.Page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
        clear();
    }

    private void clear() {
        idTextField.clear();
        nameTextField.clear();
        phoneTextField.clear();
        addressTextArea.clear();
    }

    @FXML
    private void addButtonOnAction(ActionEvent e) throws IOException {
        // TODO: INSERT
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }
}
