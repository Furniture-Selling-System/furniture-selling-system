package org.furniture.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.furniture.UIManager;
import org.furniture.enums.Page;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class CreateNewOrderController extends AbstractPageController {

    @FXML private ComboBox<String> selectCustomerComboBox;
    @FXML private TextField customerIdTextField;
    @FXML private TextField customerNameTextField;
    @FXML private TextField customerPhoneTextField;
    @FXML private TextArea customerAddressTextArea;
    @FXML private ComboBox<String> selectFurnitureComboBox;
    @FXML private TextField quantityTextField;
    @FXML private Button addButton;
    @FXML private TextField totalPriceTextField;
    @FXML private TableView<Furniture> furnitureTableView;
    @FXML private TableColumn<Furniture, String> productNameTableColumn;
    @FXML private TableColumn<Furniture, String> priceTableColumn;
    @FXML private TableColumn<Furniture, String> quantityTableColumn;
    @FXML private TableColumn<Furniture, String> totalPriceTableColumn;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;

    private ObservableList<Customer> customers;
    private ObservableList<Furniture> furnitures;
    
    private ObservableList<Furniture> orderingFurniture;
    private ObservableList<String> orderingFurnitureQty;

    private Customer selectingCustomer;
    private Furniture selectingFurniture;

    private Order order;

    @Override
    protected void initialize() {
        setUpClear();
        clear();
        getData();
        setTableView();
        showCustomers();
    }

    private void setTableView() {
        furnitureTableView.setItems(orderingFurniture);
        productNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            }
        });
        priceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(String.valueOf(arg0.getValue().getPrice()));
            }
            
        });

        quantityTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                for (Furniture f : orderingFurniture) {
                    if (f.getName().equals(arg0.getValue().getName())) {
                        return new SimpleStringProperty(orderingFurnitureQty.get(orderingFurniture.indexOf(f)));
                    }
                }
                return null;
            }
            
        });

        totalPriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                for (Furniture f : orderingFurniture) {
                    if (f.getName().equals(arg0.getValue().getName())) {
                        double totalPrice = arg0.getValue().getPrice() * Integer.parseInt(orderingFurnitureQty.get(orderingFurniture.indexOf(f)));
                        return new SimpleStringProperty(String.valueOf(totalPrice));
                    }
                }
                return null;
            }
            
        });
        
    }

    private void getData() {
        List<Customer> cList = DBConnect.getCustomers();
        customers = FXCollections.observableList(cList);

        List<Furniture> fList = DBConnect.getFurnituresList();
        furnitures = FXCollections.observableList(fList);
        System.out.println(furnitures);
    }

    private void setUpClear() {
        selectCustomerComboBox.getItems().clear();
        customerIdTextField.clear();
        customerNameTextField.clear();
        customerPhoneTextField.clear();
        customerAddressTextArea.clear();
        selectFurnitureComboBox.getItems().clear();
        quantityTextField.clear();
        totalPriceTextField.clear();

        selectFurnitureComboBox.setDisable(true);
        quantityTextField.setDisable(true);
        addButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void clear() {

        if (customers != null) {
            customers.clear();
        }

        if (furnitures != null) {
            furnitures.clear();
        }
    }

    private void showCustomers() {
        List<String> cName = new ArrayList<>();
        for (Customer customer : customers) {
            cName.add(customer.getName());
        }

        selectCustomerComboBox.getItems().setAll(cName);
    }

    @FXML
    private void selectCustomerComboBoxOnAction(ActionEvent e) {
        selectingCustomer = null;
        for (Customer customer : customers) {
            if (customer.getName().equals(selectCustomerComboBox.getSelectionModel().getSelectedItem())) {
                selectingCustomer = customer;
                break;
            }
        }

        if (selectingCustomer == null) {
            // TODO
        } else {
            customerIdTextField.setText(selectingCustomer.getId());
            customerNameTextField.setText(selectingCustomer.getName());
            customerPhoneTextField.setText(selectingCustomer.getPhone());
            customerAddressTextArea.setText(selectingCustomer.getAddress());
        }
        
        selectFurnitureComboBox.setDisable(false);
        quantityTextField.setDisable(false);
        addButton.setDisable(false);

        quantityTextField.setText("1");

        showFurniture();
    }

    private void showFurniture() {
        List<String> fName = new ArrayList<>();
        for (Furniture furniture : furnitures) {
            fName.add(furniture.getName());
        }

        selectFurnitureComboBox.getItems().setAll(fName);
    }

    @FXML
    private void selectFurnitureComboBoxOnAction(ActionEvent e) {
        selectingFurniture = null;
        for (Furniture furniture : furnitures) {
            if (furniture.getName().equals(selectFurnitureComboBox.getSelectionModel().getSelectedItem())) {
                selectingFurniture = furniture;
                break;
            }
        }
        
        quantityTextField.setText("1");
    }

    @FXML
    private void quantityTextFieldOnAction(ActionEvent e) {
        double totalPrice = Integer.parseInt(quantityTextField.getText()) * selectingFurniture.getPrice();
        totalPriceTextField.setText(String.valueOf(totalPrice));
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.NEW_ORDER_PAGE);
    }

    @FXML
    private void createButtonOnAction(ActionEvent e) throws IOException {
        // TODO : SQL
        boolean result = true;
            if (result == true) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
                initialize();
            } else {
                
            }
        UIManager.setPage(Page.NEW_ORDER_PAGE);
    }

    @FXML
    private void addButtonOnAction(ActionEvent e) {
        if (selectingCustomer != null && selectingFurniture != null) {
            orderingFurniture.add(selectingFurniture);
            orderingFurnitureQty.add(String.valueOf(Integer.parseInt(quantityTextField.getText())));
        }
    }

    @FXML
    private void deleteButtonOnAction(ActionEvent e) {
        int index = orderingFurniture.indexOf(furnitureTableView.getSelectionModel().getSelectedItem());
        orderingFurniture.remove(index);
        
    }
    
}
