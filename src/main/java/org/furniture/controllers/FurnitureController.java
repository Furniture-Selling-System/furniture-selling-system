package org.furniture.controllers;

import java.io.IOException;
import java.util.List;

import javafx.scene.input.KeyEvent;
import org.furniture.UIManager;
import org.furniture.enums.Page;
import org.furniture.exceptions.InvalidQuantityException;
import org.furniture.models.Furniture;
import org.furniture.services.DBConnect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class FurnitureController extends AbstractPageController {

    @FXML TextField searchTextField;
    @FXML ListView<Furniture> furnitureListView;
    @FXML Button editButton;
    @FXML TextField furnitureIdTextField;
    @FXML TextField furnitureNameTextField;
    @FXML TextField furniturePriceTextField;

    private ObservableList<Furniture> data;
    private Furniture selectingFurniture;

    @Override
    protected void initialize() {
        getData();
        showMaterialListView();
        clearSelectMaterial();
        handleSelectedMaterialListView();
    }

    public void getData() {
        List<Furniture> furnitures = DBConnect.getFurnitureList();
        data = FXCollections.observableList(furnitures);
    }

    private void clearSelectMaterial() {
        furnitureListView.refresh();
        furnitureIdTextField.clear();
        furnitureNameTextField.clear();
        furniturePriceTextField.clear();
    }

    @FXML
    private void confirmButtonOnAction(ActionEvent e) throws InvalidQuantityException {
        selectingFurniture = furnitureListView.getSelectionModel().getSelectedItem();
        selectingFurniture.setPrice(Integer.parseInt(furniturePriceTextField.getText()));
        DBConnect.updateFurniture(selectingFurniture);

        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Furniture has been successfully edited.");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();

        clearSelectMaterial();
        showMaterialListView();
    }


    @FXML
    public void addFurnitureButtonOnAction(ActionEvent actionEvent) throws IOException {
        UIManager.setPage(Page.CREATE_FURNITURE_PAGE);
    }

    private void showMaterialListView() {
        furnitureListView.setItems(data);
    }

    private void handleSelectedMaterialListView() {
        furnitureListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSelectedMaterial(newValue));
    }

    private void showSelectedMaterial(Furniture furniture) {
        furnitureIdTextField.setText(furniture.getId());
        furnitureNameTextField.setText(furniture.getName());
        furniturePriceTextField.setText(String.valueOf(furniture.getPrice()));
    }

    @FXML
    private void searchTextFieldOnAction(KeyEvent e) {
        List<Furniture> furniture = DBConnect.getFurnituresListByName(searchTextField.getText());
        data = FXCollections.observableList(furniture);
        showMaterialListView();
        if (searchTextField.getText().isEmpty())
            initialize();
    }

}
