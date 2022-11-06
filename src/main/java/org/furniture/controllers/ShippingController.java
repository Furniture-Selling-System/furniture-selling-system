// package org.furniture.controllers;

// import java.util.List;

// import org.furniture.enums.OrderStatus;
// import org.furniture.models.Furniture;
// import org.furniture.models.Order;
// import org.furniture.services.DBConnect;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.TextArea;
// import javafx.scene.control.TextField;
// import javafx.scene.control.Alert.AlertType;

// public class ShippingController extends AbstractPageOrderController {

//     @FXML TextField searchTextField;
//     @FXML TableView<Order> orderTableView;
//     @FXML TableColumn<Order, String> orderIdTableColumn;
//     @FXML TableColumn<Order, String> customerNameTableColumn;
//     @FXML TableColumn<Order, String> orderDateTableColumn;
//     @FXML TableView<Furniture> furnitureTableView;
//     @FXML TableColumn<Order, String> furnitureNameTableColumn;
//     @FXML TableColumn<Order, String> furnitureQuantityTableColumn;
//     @FXML TextField customerNameTextField;
//     @FXML TextField customerPhoneTextFIeld;
//     @FXML TextArea customerAddressTextArea;

//     private ObservableList<Order> oList;
//     private ObservableList<Furniture> fList;

//     @Override
//     protected void confirmButtonOnAction(ActionEvent e) {
//         boolean result = true;
//             if (result == true) {
//                 Alert alert = new Alert(AlertType.INFORMATION);
//                 alert.getButtonTypes().setAll(ButtonType.OK);
//                 alert.showAndWait();
//                 initialize();
//             } else {
                
//             }
//     }

//     @Override
//     protected void initialize() {
//         setUpClear();
//         clear();
//         getData();
//         setTableView();
//         showOrders();
//     }
    
//     private void setUpClear() {
//         searchTextField.clear();
//         confirmButton.setDisable(true);
//     }

//     private void clear() {

//         if (oList != null) {
//             oList.clear();
//         }

//         if (fList != null) {
//             fList.clear();
//         }
//     }

//     private void getData() {
//         List<Order> orderList = DBConnect.getSaleOrdersByStatus(OrderStatus.SHIPPING);
//         oList = FXCollections.observableList(orderList);
//     }

//     private void showOrders() {
//         List<Order> orderList = 
//     }

// }
