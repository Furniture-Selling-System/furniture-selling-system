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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
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

public class ConstructingController extends AbstractPageOrderController {
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
    @FXML private Button confirmButton;

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
        List<Order> orderList = DBConnect.getSaleOrdersByStatus(OrderStatus.CONSTURCTING);
        data = FXCollections.observableList(orderList);
    }

    private void setUpClear() {
        searchTextField.clear();
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
        if ((selectingOrder != null) || (selectingOrder != newSelectingOrder)) {
            selectingOrder = newSelectingOrder;
        } else {
            return;
        }

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
        List<Furniture> furnitures = DBConnect.getFurnitureListByOrderID(selectingOrder.getId());
        HashMap<Material, Integer> materials = new HashMap<>();

        for (Furniture f : furnitures) {
            HashMap<Material, Integer> mHashMap = f.getMaterials();
            for (Material m : mHashMap.keySet()) {
                if (materials.get(m) == null) {
                    materials.put(m, mHashMap.get(m));
                } else {
                    materials.put(m, materials.get(m) + mHashMap.get(m));
                }
            }
        }

        ObservableSet<Material> mSet = FXCollections.observableSet(materials.keySet());
        List<Material> oList = new ArrayList<>(mSet);
        oList.sort(new Comparator<Material>() {

            @Override
            public int compare(Material o1, Material o2) {
                if (Integer.parseInt(o1.getId()) >= Integer.parseInt(o2.getId())) return 1;
                return -1;
            }
            
        });
        mList = FXCollections.observableList(oList);
        
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
                for (Material m : materials.keySet()) {
                    String mName = arg0.getValue().getName();
                    if (m.getName().equals(mName)) {
                        return new SimpleStringProperty(String.valueOf(materials.get(m)));
                    }
                }
                return null;
            }
        });
    }
    
    @Override
    protected void confirmButtonOnAction(ActionEvent e) {
        // TODO
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
