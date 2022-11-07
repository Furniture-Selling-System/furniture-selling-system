package org.furniture.controllers.add;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.OrderStatus;
import org.furniture.enums.Page;
import org.furniture.exceptions.DuplicateDataException;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class AddOrderController extends AbstractPageController {

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

    @Override
    protected void initialize() {
        orderingFurniture = FXCollections.observableArrayList();
        orderingFurnitureQty = FXCollections.observableArrayList();

        setUpClear();
        clear();
        getData();
        showCustomers();
        setTableView();
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
    }

    private void clear() {

        if (customers != null) {
            customers.clear();
        }

        if (furnitures != null) {
            furnitures.clear();
        }
    }

    private void getData() {
        List<Customer> cList = DBConnect.getCustomers();
        customers = FXCollections.observableList(cList);

        List<Furniture> fList = DBConnect.getFurnitureList();
        furnitures = FXCollections.observableList(fList);
    }



    private void setTableView() {
        furnitureTableView.setItems(orderingFurniture);
        productNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
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
                for (int i = 0 ; i < orderingFurniture.size() ; ++i) {
                    if (orderingFurniture.get(i).getName().equals(arg0.getValue().getName())) {
                        return new SimpleStringProperty(orderingFurnitureQty.get(i));
                    }
                }
                return null;
            }
            
        });

        totalPriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                for (int i = 0 ; i < orderingFurniture.size() ; ++i) {
                    if (orderingFurniture.get(i).getName().equals(arg0.getValue().getName())) {
                        double totalPrice = arg0.getValue().getPrice() * Integer.parseInt(orderingFurnitureQty.get(i));
                        return new SimpleStringProperty(String.valueOf(totalPrice));
                    }
                }
                return null;
            }
            
        });
        
    }

    private void showCustomers() {
        List<String> cName = new ArrayList<>();
        for (Customer customer : customers) {
            cName.add(customer.getName());
        }

        selectCustomerComboBox.getItems().setAll(cName);
    }

    // @FXML
    // private void selectCustomerComboBoxOnKeyTyped(KeyEvent e) {
    //     List<Customer> customers = DBConnect.getCustomersByName(selectCustomerComboBox.getSelectionModel().getSelectedItem());
    //     ObservableList<Customer> data = FXCollections.observableList(customers);
    //     List<String> cName = new ArrayList<>();
    //     for (Customer c : data) {
    //         cName.add(c.getName());
    //     }
        
    //     selectCustomerComboBox.setItems(FXCollections.observableList(cName));
    // }

    @FXML
    private void selectCustomerComboBoxOnAction(ActionEvent e) {
        selectingCustomer = null;
        orderingFurniture.clear();
        orderingFurnitureQty.clear();
        for (Customer customer : customers) {
            if (customer.getName().equals(selectCustomerComboBox.getSelectionModel().getSelectedItem())) {
                selectingCustomer = customer;
                break;
            }
        }

        if (selectingCustomer == null) {
            
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
        quantityTextFieldOnAction(null);
    }

    @FXML
    private void quantityTextFieldOnAction(KeyEvent e) {
        try {
            if ( Integer.parseInt(quantityTextField.getText()) <= 0 || Integer.parseInt(quantityTextField.getText()) > 99) throw new Exception();
            double totalPrice = Integer.parseInt(quantityTextField.getText()) * selectingFurniture.getPrice();
            totalPriceTextField.setText(String.valueOf(totalPrice));
        } catch (Exception exception) {
            totalPriceTextField.setText("");
        }
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.NEW_ORDER_PAGE);
    }

    @FXML
    private void createButtonOnAction(ActionEvent e) throws IOException {
        Order o = new Order(String.valueOf(DBConnect.getOrderLastID() + 1), selectingCustomer.getName(), Double.parseDouble(totalPriceTextField.getText()), selectingCustomer.getAddress(), new Date(), OrderStatus.WAITING, selectingCustomer);
        
        for (int i = 0 ; i < orderingFurniture.size() ; ++i) {
            o.addFurniture(orderingFurniture.get(i), Integer.parseInt(orderingFurnitureQty.get(i)));
        }
        DBConnect.insertOrder(o);
        Alert alert = new Alert(AlertType.INFORMATION, "Order number " + o.getId() + " is successfully added.");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
        initialize();
        UIManager.setPage(Page.NEW_ORDER_PAGE);
    }

    @FXML
    private void addButtonOnAction(ActionEvent e) {
        if (selectingCustomer != null && selectingFurniture != null) {
            try {
                if (Integer.valueOf(quantityTextField.getText()) <= 0 || Integer.valueOf(quantityTextField.getText()) > 99) throw new NumberFormatException();
                if (orderingFurniture.contains(selectingFurniture)) throw new DuplicateDataException();
                orderingFurniture.add(selectingFurniture);
                orderingFurnitureQty.add(quantityTextField.getText());
            } catch (NumberFormatException exception) {
                new Alert(AlertType.WARNING, "Value must be integer between 1-99.").showAndWait();
            } catch (DuplicateDataException exception) {
                new Alert(AlertType.WARNING, "You have added this furniture already.").showAndWait();
            }
        } else {
            new Alert(AlertType.WARNING, "No furnitures found.").showAndWait();
        }
    }

    @FXML
    private void deleteButtonOnAction(ActionEvent e) {
        orderingFurniture.remove(furnitureTableView.getSelectionModel().getSelectedItem());
    }
    
}
