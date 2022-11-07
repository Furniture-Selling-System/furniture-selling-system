package org.furniture.controllers;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.*;
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
        if (validation()){
            try {
                selectingFurniture = furnitureListView.getSelectionModel().getSelectedItem();
                selectingFurniture.setPrice(Integer.parseInt(furniturePriceTextField.getText()));
                DBConnect.updateFurniture(selectingFurniture);
                clearSelectMaterial();
                showMaterialListView();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Furniture has been updated.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                throw new RuntimeException(ex);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Furniture can not update.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
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

    private boolean validation(){
        if(furniturePriceTextField.getLength() > 5)
            return false;
        return true;
    }

}
