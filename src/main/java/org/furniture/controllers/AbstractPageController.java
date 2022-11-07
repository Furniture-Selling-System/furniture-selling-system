package org.furniture.controllers;

import java.io.IOException;

import org.furniture.UIManager;
import org.furniture.enums.Page;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class AbstractPageController {
    @FXML protected Button newOrdersNavigationButton;
    @FXML protected Button constructingNavigationButton;
    @FXML protected Button shippingNavigationButton;
    @FXML protected Button confirmingNavigationButton;
    @FXML protected Button orderHistoryNavigationButton;
    @FXML protected Button customersNavigationButton;
    @FXML protected Button materialsNavigationButton;
    @FXML protected Button furnitureNavigationButton;
    @FXML protected Button checkMaterialNavigationButton;
    
    @FXML
    abstract protected void initialize();

    @FXML
    protected void newOrdersNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.NEW_ORDER_PAGE);
    }

    @FXML
    protected void constructingNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CONSTRUCT_PAGE);
    }

    @FXML
    protected void shippingNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.SHIP_PAGE);
    }

    @FXML
    protected void confirmingNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CONFIRM_PAGE);
    }

    @FXML
    protected void orderHistoryNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.ORDER_HISTORY_PAGE);
    }

    @FXML
    protected void customersNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }

    @FXML
    protected void materialsNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.MATERIALS_PAGE);
    }

    @FXML
    protected void furnitureNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.FURNITURE_PAGE);
    }

    @FXML
    protected void checkMaterialNavigationButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CHECK_MATERIAL_PAGE);
    }
}
