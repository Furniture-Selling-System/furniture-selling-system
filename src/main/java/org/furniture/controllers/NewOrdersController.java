package org.furniture.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import org.furniture.UIManager;
import org.furniture.enums.OrderStatus;
import org.furniture.enums.Page;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.models.Order;
import org.furniture.services.DBConnect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class NewOrdersController extends AbstractPageOrderController {
    @FXML private TextField searchTextField;
    @FXML private TableView<Order> ordersTableView;
    @FXML private TableColumn<Order, String> orderIdTableColumn;
    @FXML private TableColumn<Order, String> customerNameTableColumn;
    @FXML private TableColumn<Order, String> orderDateTableColumn;
    @FXML private TableView<Furniture> furnitureTableView;
    @FXML private TableColumn<Furniture, String> furnitureIdTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureNameTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureQuantityTableColumn;
    @FXML private TableView<Material> materialsTableView;
    @FXML private TableColumn<Material, String> materialIdTableColumn;
    @FXML private TableColumn<Material, String> materialNameTableColumn;
    @FXML private TableColumn<Material, String> materialQuantityTableColumn;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button createButton;

    private ObservableList<Order> data;
    private Order selectingOrder;

    private ObservableList<Furniture> fList;
    private ObservableList<Material> mList;

    @Override
    protected void initialize() {
        setUpClear();
        clear();
        getData();
        showOrders();
    }

    private void getData() {
        List<Order> orderList = DBConnect.getSaleOrdersByStatus(OrderStatus.WAITING);
        data = FXCollections.observableList(orderList);
    }

    private void setUpClear() {
        searchTextField.clear();
        cancelButton.setDisable(true);
        confirmButton.setDisable(true);
    }

    private void clear() {

        if (data != null) {
            data.clear();
        }

        if (fList != null) {
            fList.clear();
        }

        if (mList != null) {
            mList.clear();
        }
    }

    private void showOrders() {
        ordersTableView.setItems(data);
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
        if (newSelectingOrder instanceof Order) {
            if (selectingOrder == null) {
                selectingOrder = newSelectingOrder;
            } else if (selectingOrder.getId().equals(newSelectingOrder.getId())) {
                return;
            } else {
                selectingOrder = newSelectingOrder;
            }
        } else {
            return;
        }

        cancelButton.setDisable(false);
        confirmButton.setDisable(false);

        showFurniture();
        showMaterial();
    }

    private void showFurniture() {
        HashMap<Furniture, Integer> furniture = DBConnect.getFurnitureAmountByOrderID(selectingOrder.getId());
        ObservableSet<Furniture> fSet = FXCollections.observableSet(furniture.keySet());
        List<Furniture> oldFList = new ArrayList<>(fSet);
        oldFList.sort(new Comparator<Furniture>() {
            @Override
            public int compare(Furniture o1, Furniture o2) {
                if (Integer.parseInt(o1.getId()) >= Integer.parseInt(o2.getId())) return 1;
                return -1;
            }
        });

        fList = FXCollections.observableList(oldFList);
        furnitureTableView.setItems(fList);
        furnitureIdTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            } 
        });
        furnitureNameTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            } 
        });
        furnitureQuantityTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                for (Furniture f : furniture.keySet()) {
                    String fName = arg0.getValue().getName();
                    if (f.getName().equals(fName)) {
                        return new SimpleStringProperty(String.valueOf(furniture.get(f)));
                    }
                }
                return null;
            }
        });
    }

    private void showMaterial() {
        HashMap<Material, Integer> materialsBOM = DBConnect.getMaterialsByOrderId(selectingOrder.getId());

        mList = FXCollections.observableArrayList(materialsBOM.keySet());
        mList.sort(new Comparator<Material>() {

            @Override
            public int compare(Material o1, Material o2) {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
            
        });

        HashMap<String, Integer> mId = new HashMap<>();
        for (Material m : materialsBOM.keySet()) {
            mId.put(m.getId(), materialsBOM.get(m));
        }

        materialsTableView.setItems(mList);
        materialIdTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Material,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Material, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            } 
        });
        materialNameTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Material,String>,ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Material, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            }
        });
        materialQuantityTableColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Material,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Material, String> arg0) {
                for (String m : mId.keySet()) {
                    if (m.equals(arg0.getValue().getId())) {
                        return new SimpleStringProperty(String.valueOf(mId.get(m)));
                    }
                }
                return null;
            }
            
        });
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "You are cancelling order number " + selectingOrder.getId() + ". Are you sure?", ButtonType.NO, ButtonType.YES);
        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType t) {
                if (t == ButtonType.YES) {
                    selectingOrder.setStatus(OrderStatus.CANCELLED);
                    DBConnect.updateOrderStatus(selectingOrder);
                    Alert alert = new Alert(AlertType.INFORMATION, "Order number " + selectingOrder.getId() + " has been " + OrderStatus.CANCELLED + ".");
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    alert.showAndWait();
                    initialize();
                }
            }
        });

    }

    @FXML
    private void createButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CREATE_ORDER_PAGE);
    }

    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        if(DBConnect.checkOrderCanBeConstruction(selectingOrder.getId())) {
            try {
                selectingOrder.setStatus(OrderStatus.CONSTURCTING);
                DBConnect.updateOrderStatus(selectingOrder);
                DBConnect.changeSaleOrderToConstruction(selectingOrder.getId());

                Alert alert = new Alert(AlertType.INFORMATION, "Order number " + selectingOrder.getId() + " has successfully updated to " + OrderStatus.CONSTURCTING + ".");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
                initialize();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }   else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order number " + selectingOrder.getId() + " can't construction because material not enough");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    protected void searchTextFieldOnAction(KeyEvent e) {
        clear();
        getData();

        List<Order> removing = new ArrayList<>();
        for (Order order : data) {
            if (!(order.getName().contains(searchTextField.getText().trim()))) {
                removing.add(order);
            }
        }

        for (Order order : removing) {
            data.remove(order);
        }

        showOrders();
    }
}
