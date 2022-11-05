package org.furniture.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.furniture.services.DBConnect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    ListView<String> materialListView;

    @FXML
    Button confirmButton;

    private ArrayList<String> materialsArrayList;

    @Override
    protected void initialize() {
        materialsArrayList = DBConnect.getMaterials();
        showMaterialListView();
        clearSelectMaterial();
        handleSelectedMaterialListView();
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
        if (confirmButton.getText().equals("EDIT")) {
            DBConnect.queryUpdate("UPDATE material\n" +
                    "SET name='" + materialName + "',quantity='" + materialQuantity + "',minimum='" + materialMinimum + "'\n" +
                    "WHERE id=" + materialID);
        } else {
            DBConnect.queryUpdate("INSERT INTO material(name,quantity,minimum)\n" +
                    "VALUES ('" + materialName + "','" + materialQuantity + "','" + materialMinimum + "')");
        }
        clearSelectMaterial();
        showMaterialListView();
    }

    private void showMaterialListView() {
        materialListView.getItems().clear();
        materialsArrayList = DBConnect.getMaterials();
        materialListView.getItems().addAll(materialsArrayList);
        materialListView.refresh();
    }

    private void handleSelectedMaterialListView() {
        confirmButton.setText("EDIT");
        materialListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedMaterial(newValue));
    }

    private void showSelectedMaterial(String material) {
        ResultSet rs = null;
        try {
            rs = DBConnect.query("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                    "WHERE id=" + material.charAt(0));
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
        materialListView.getItems().clear();
        materialsArrayList = DBConnect.getMaterialsByName(searchTextField.getText());
        materialListView.getItems().addAll(materialsArrayList);
        materialListView.refresh();
        if(searchTextField.getText().isEmpty())
            initialize();
    }


}
