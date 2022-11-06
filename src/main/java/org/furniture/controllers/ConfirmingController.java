package org.furniture.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.furniture.enums.OrderStatus;
import org.furniture.models.Furniture;
import org.furniture.models.Order;
import org.furniture.services.DBConnect;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class ConfirmingController extends AbstractPageOrderController {

    @FXML private TextField searchTextField;
    @FXML private TableView<Order> ordersTableView;
    @FXML private TableColumn<Order, String> orderIdTableColumn;
    @FXML private TableColumn<Order, String> customerNameTableColumn;
    @FXML private TableColumn<Order, String> orderDateTableColumn;
    @FXML TableView<Furniture> furnitureTableView;
    @FXML TableColumn<Furniture, String> furnitureNameTableColumn;
    @FXML TableColumn<Furniture, String> furnitureQuantityTableColumn;

    private ObservableList<Order> oList;
    private ObservableList<Furniture> fList;
    private Order selectingOrder;

    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        boolean result = true;
        if (result == true) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            initialize();
        } else {
            
        }
    }

    @Override
    protected void initialize() {
        setUpClear();
        clear();
        getData();
        showOrders();
    }

    private void setUpClear() {
        searchTextField.clear();
        confirmButton.setDisable(true);
    }

    private void clear() {
        
        if (oList != null) {
            oList.clear();
        }

        if (fList != null) {
            fList.clear();
        }
    }

    private void getData() {
        List<Order> orderList = DBConnect.getSaleOrdersByStatus(OrderStatus.CONFIRMING);
        oList = FXCollections.observableList(orderList);
    }

    private void showOrders() {
        ordersTableView.setItems(oList);
        orderIdTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            } 
        });
        customerNameTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            } 
        });
        orderDateTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getCreationDateTime().toString());
            } 
        });
    }

    @FXML
    private void ordersTableViewOnMouseClicked(MouseEvent e) {
        Order newSelectingOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if ((selectingOrder != null) || (selectingOrder != newSelectingOrder)) {
            selectingOrder = newSelectingOrder;

            confirmButton.setDisable(false);

            HashMap<Furniture, Integer> f = DBConnect.getFurnitureAmountByOrderID(selectingOrder.getId());
            fList = FXCollections.observableList(new ArrayList<>(f.keySet()));
            furnitureTableView.setItems(fList);
            furnitureNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                    return new SimpleStringProperty(arg0.getValue().getName());
                }
                
            });
            furnitureQuantityTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                    for (Furniture furniture : f.keySet()) {
                        String fName = arg0.getValue().getName();
                        if (furniture.getName().equals(fName)) {
                            return new SimpleStringProperty(String.valueOf(f.get(furniture)));
                        }
                    }
                    return null;
                }
            });
        } else {
            
        }

    }

}
