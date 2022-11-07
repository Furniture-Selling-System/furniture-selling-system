package org.furniture.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.furniture.enums.OrderStatus;
import org.furniture.models.Customer;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;

public class ShippingController extends AbstractPageOrderController {

    @FXML TextField searchTextField;
    @FXML TableView<Order> orderTableView;
    @FXML TableColumn<Order, String> orderIdTableColumn;
    @FXML TableColumn<Order, String> customerNameTableColumn;
    @FXML TableColumn<Order, String> orderDateTableColumn;
    @FXML TableView<Furniture> furnitureTableView;
    @FXML TableColumn<Furniture, String> furnitureNameTableColumn;
    @FXML TableColumn<Furniture, String> furnitureQuantityTableColumn;
    @FXML TextField customerNameTextField;
    @FXML TextField customerPhoneTextField;
    @FXML TextArea customerAddressTextArea;

    private ObservableList<Order> oList;
    private ObservableList<Furniture> fList;
    
    private Order selectingOrder;

    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        selectingOrder.setStatus(OrderStatus.CONFIRMING);
        DBConnect.updateOrderStatus(selectingOrder);
        Alert alert = new Alert(AlertType.INFORMATION, "Order number " + selectingOrder.getId() + " has successfully updated to " + OrderStatus.CONFIRMING + ".");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
        initialize();
    }

    @Override
    protected void initialize() {
        setUpClear();
        clear();
        getData();
        setTableView();
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
        List<Order> orderList = DBConnect.getSaleOrdersByStatus(OrderStatus.SHIPPING);
        oList = FXCollections.observableList(orderList);
    }

    private void setTableView() {
        orderTableView.setItems(oList);
        orderIdTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            }
            
        });
        customerNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            }
            
        });
        orderDateTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Order, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getCreationDateTime().toString());
            }
            
        });
        
    }

    @FXML
    private void ordersTableViewOnMouseClicked(MouseEvent e) {
        Order newSelectingOrder = orderTableView.getSelectionModel().getSelectedItem();
        if ((selectingOrder != null) || (selectingOrder != newSelectingOrder)) {
            selectingOrder = newSelectingOrder;

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

            confirmButton.setDisable(false);
            customerNameTextField.setText(selectingOrder.getName());
            
            List<Customer> c = DBConnect.getCustomers();
            Customer selectingCustomer = null;
            for (Customer customer : c) {
                if (customer.getName().equals(selectingOrder.getName())) {
                    selectingCustomer = customer;
                    break;
                }
            }

            customerPhoneTextField.setText(selectingCustomer.getName());
            customerAddressTextArea.setText(selectingOrder.getAddress());
        }
    }

    @FXML
    private void searchTextFieldOnKeyTyped(KeyEvent e) {
        clear();
        getData();

        List<Order> removing = new ArrayList<>();
        for (Order order : oList) {
            if (!(order.getName().contains(searchTextField.getText().trim()))) {
                removing.add(order);
            }
        }

        for (Order order : removing) {
            oList.remove(order);
        }

        setTableView();
    }

}
