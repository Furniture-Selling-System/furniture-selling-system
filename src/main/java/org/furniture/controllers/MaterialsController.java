package org.furniture.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import org.furniture.UIManager;
import org.furniture.enums.Page;
import org.furniture.exceptions.InvalidQuantityException;
import org.furniture.models.Material;
import org.furniture.services.DBConnect;

import java.io.IOException;
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
    private Material selectingMaterial;

    @Override
    protected void initialize() {
        getData();
        showMaterialListView();
        clearSelectMaterial();
        handleSelectedMaterialListView();
    }

    public void getData() {
        List<Material> materials = DBConnect.getMaterialsList();
        data = FXCollections.observableList(materials);
    }

    private void clearSelectMaterial() {
        materialListView.refresh();
        idTextField.clear();
        nameTextField.clear();
        quantityTextField.clear();
        minimumTextField.clear();
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent e) throws InvalidQuantityException {
        if (validation()) {
            try {
                selectingMaterial = materialListView.getSelectionModel().getSelectedItem();
                selectingMaterial.setMinimum(Integer.parseInt(minimumTextField.getText()));
                selectingMaterial.setQuantity(Integer.parseInt(quantityTextField.getText()));
                DBConnect.updateMaterial(selectingMaterial);
                clearSelectMaterial();
                showMaterialListView();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Material has been updated.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Material can not update.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
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
        idTextField.setText(material.getId());
        nameTextField.setText(material.getName());
        quantityTextField.setText(String.valueOf(material.getQuantity()));
        minimumTextField.setText(String.valueOf(material.getMinimum()));
    }

    @FXML
    private void searchTextFieldOnAction(KeyEvent e) {
        List<Material> materials = DBConnect.getMaterialsListByName(searchTextField.getText());
        data = FXCollections.observableList(materials);
        showMaterialListView();
        if (searchTextField.getText().isEmpty())
            initialize();
    }

    private boolean validation() {
        if (nameTextField.getText().trim().isEmpty()) {
            return false;
        }
        if (minimumTextField.getText().trim().isEmpty() || minimumTextField.getLength() > 3) {
            return false;
        }
        if (quantityTextField.getText().trim().isEmpty() || quantityTextField.getLength() > 4) {
            return false;
        }
        return isInt(quantityTextField.getText()) && isInt(minimumTextField.getText());
    }

    private boolean isInt(String str) {
        return str.matches("\\d+");
    }
}
