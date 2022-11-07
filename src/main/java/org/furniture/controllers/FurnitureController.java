package org.furniture.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.furniture.UIManager;
import org.furniture.enums.Page;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.services.DBConnect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class FurnitureController extends AbstractPageController {

    @FXML TextField searchTextField;
    @FXML ListView<String> furnitureListView;
    @FXML ComboBox<String> materialComboBox;
    @FXML TextField quantityTextField;
    @FXML Button addButton;
    @FXML TableView<Material> materialTableView;
    @FXML TableColumn<Material, String> materialNameTableColumn;
    @FXML TableColumn<Material, String> quantityTableColumn;
    @FXML Button deleteButton;
    @FXML Button editButton;
    @FXML TextField furnitureIdTextField;
    @FXML TextField furnitureNameTextField;
    @FXML TextField furniturePriceTextField;

    private ObservableList<Furniture> furniture;

    private Furniture selectingFurniture;

    @Override
    protected void initialize() {
        furniture = FXCollections.observableArrayList();

        clear();
        searchTextFieldOnAction(null);
    }

    private void clear() {
        searchTextField.clear();
        furniture.clear();
    }

    @FXML
    private void searchTextFieldOnAction(ActionEvent e) {
        furniture.clear();

        furniture = FXCollections.observableList(DBConnect.getFurnitureList());

        List<Furniture> removing = new ArrayList<>();
        for (Furniture f : furniture) {
            if (!(f.getName().contains(searchTextField.getText().trim()))) {
                removing.add(f);
            }
        }

        for (Furniture f : removing) {
            furniture.remove(f);
        }

        ObservableList<String> fStr = FXCollections.observableArrayList();
        for (Furniture f : furniture) {
            fStr.add(f.getName());
        }

        furnitureListView.setItems(fStr);
    }

    @FXML
    private void furnitureListViewOnMouseClicked(MouseEvent e) {
        String selectingFurnitureString = furnitureListView.getSelectionModel().getSelectedItem();
        for (Furniture f : furniture) {
            if (f.getName().equals(selectingFurnitureString)) {
                selectingFurniture = f;
            }
        }

        furnitureIdTextField.setText(selectingFurniture.getId());
        furnitureNameTextField.setText(selectingFurniture.getName());
        furniturePriceTextField.setText(String.valueOf(selectingFurniture.getPrice()));
    }
    
    @FXML
    private void addButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CREATE_FURNITURE_PAGE);
    }

    @FXML
    private void editButtonOnAction(ActionEvent e) {
        //TODO:Insert
    }

}
