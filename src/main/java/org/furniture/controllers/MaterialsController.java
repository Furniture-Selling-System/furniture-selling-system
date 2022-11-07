package org.furniture.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import org.furniture.UIManager;
import org.furniture.enums.Page;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.models.Order;
import org.furniture.services.DBConnect;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialsController extends AbstractPageController {
    @FXML
    TextField idTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField quantityTextField;

    @FXML
    TextField minimumTextField;
    @FXML
    TextField searchTextField;

    @FXML
    ListView<Material> materialListView;

    @FXML
    Button confirmButton;

    @FXML
    Button addMaterialButton;

    private ObservableList<Material> data;

    @Override
    protected void initialize() {
        getData();
        showMaterialListView();
        clearSelectMaterial();
        handleSelectedMaterialListView();
    }

    public void getData(){
        List<Material> materials = DBConnect.getMaterialsList();
        data = FXCollections.observableList(materials);
    }

    private void clearSelectMaterial(){
        idTextField.clear();
        nameTextField.clear();
        quantityTextField.clear();
        minimumTextField.clear();
    }
    
    @FXML
    private void searchTextFieldOnAction(ActionEvent e) {
        // TODO
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent e) {
        String materialID = idTextField.getText();
        String materialName = nameTextField.getText();
        int materialQuantity = Integer.parseInt(minimumTextField.getText());
        int materialMinimum = Integer.parseInt(quantityTextField.getText());
        DBConnect.queryUpdate("UPDATE material\n" +
                "SET name='" + materialName + "',quantity='" + materialQuantity + "',minimum='" + materialMinimum + "'\n" +
                "WHERE id=" + materialID);
        clearSelectMaterial();
        showMaterialListView();
    }


    @FXML
    public void addMaterialButtonOnAction(ActionEvent actionEvent) throws IOException {
        UIManager.setPage(Page.CREATE_MATERIAL_PAGE);
    }

    private void showMaterialListView() {
        materialListView.setItems(data);
    }

    private void handleSelectedMaterialListView() {
        materialListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedMaterial(newValue));
    }

    private void showSelectedMaterial(Material material) {
        ResultSet rs = null;
        try {
            rs = DBConnect.query("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                    "WHERE id=" + material.getId());
            rs.next();
            idTextField.setText(rs.getString("id"));
            nameTextField.setText(rs.getString("name"));
            quantityTextField.setText(rs.getString("quantity"));
            minimumTextField.setText(rs.getString("minimum"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void searchTextFieldOnAction(KeyEvent e){
//        clearSelectMaterial();
//        getData();
//
//        List<Order> removing = new ArrayList<>();
//        for (Order order : data) {
//            if (!(order.getName().contains(searchTextField.getText().trim()))) {
//                removing.add(order);
//            }
//        }
//
//        for (Order order : removing) {
//            data.remove(order);
//        }
//
//        showOrders();
    }
}
