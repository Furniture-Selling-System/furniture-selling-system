package org.furniture.controllers.add;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.OrderStatus;
import org.furniture.enums.Page;
import org.furniture.exceptions.DuplicateDataException;
import org.furniture.models.Customer;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.models.Order;
import org.furniture.services.DBConnect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddFurnitureController extends AbstractPageController {
    @FXML
    TextField furnitureIdTextField;
    @FXML
    TextField furnitureNameTextField;
    @FXML
    TextField furniturePriceTextField;
    @FXML
    TableView<Material> materialTableView;
    @FXML
    TableColumn<Material, String> materialNameTableColumn;
    @FXML
    TableColumn<Material, String> quantityTableColumn;
    @FXML
    ComboBox<String> selectMaterialComboBox;
    @FXML
    TextField quantityTextField;
    @FXML
    Button deleteButton;
    @FXML
    Button addButton;
    private ObservableList<Material> materials;

    private ObservableList<Material> orderingMaterial;
    private ObservableList<Integer> orderingMaterialQty;

    private Material selectingMaterial;
    @Override
    protected void initialize() {
        orderingMaterial = FXCollections.observableArrayList();
        orderingMaterialQty = FXCollections.observableArrayList();
        quantityTextField.setText("1");

        setUpClear();
        clear();
        getData();
        showMaterial();
        setTableView();
    }

    private void setUpClear() {
        selectMaterialComboBox.getItems().clear();
        furnitureIdTextField.clear();
        furnitureIdTextField.clear();
        furnitureIdTextField.clear();
        quantityTextField.clear();
        deleteButton.setDisable(true);
    }

    private void clear() {
        if (materials != null)
            materials.clear();
    }

    private void getData() {
        List<Material> materialsList = DBConnect.getMaterialsList();
        materials = FXCollections.observableList(materialsList);
    }

    private void showMaterial() {
        List<String> mName = new ArrayList<>();
        for (Material material : materials) {
            mName.add(material.getName());
        }

        selectMaterialComboBox.getItems().setAll(mName);
    }

    private void setTableView() {
        materialTableView.setItems(orderingMaterial);
        materialNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Material, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Material, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            }
        });
        quantityTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Material, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Material, String> arg0) {
                for (int i = 0; i < orderingMaterial.size(); ++i) {
                    if (orderingMaterial.get(i).getName().equals(arg0.getValue().getName())) {
                        return new SimpleStringProperty(String.valueOf(orderingMaterialQty.get(i)));
                    }
                }
                return null;
            }

        });
    }

    @FXML
    private void addButtonOnAction(ActionEvent e) throws IOException {
        if (selectingMaterial != null) {
            try {
                if (Integer.valueOf(quantityTextField.getText()) <= 0 || Integer.valueOf(quantityTextField.getText()) > 99)
                    throw new NumberFormatException();
                if (orderingMaterial.contains(selectingMaterial)) throw new DuplicateDataException();
                orderingMaterial.add(selectingMaterial);
                orderingMaterialQty.add(Integer.valueOf(quantityTextField.getText()));
            } catch (NumberFormatException exception) {
                new Alert(Alert.AlertType.WARNING, "Value must be integer between 1-99.").showAndWait();
            } catch (DuplicateDataException exception) {
                new Alert(Alert.AlertType.WARNING, "You have added this materials already.").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No furnitures found.").showAndWait();
        }
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }

    public void createButtonOnAction(ActionEvent actionEvent) throws IOException {
        Furniture f = new Furniture(String.valueOf(DBConnect.getFurnitureLastID() + 1),
                furnitureNameTextField.getText(),
                Integer.parseInt(furniturePriceTextField.getText()));

        for (int i = 0; i < orderingMaterial.size(); ++i) {
            f.addMaterial(orderingMaterial.get(i), orderingMaterialQty.get(0));
        }
        DBConnect.insertFurniture(f);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Furniture number " + f.getId() + " is successfully added.");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
        initialize();
        UIManager.setPage(Page.FURNITURE_PAGE);
    }

    public void selectMaterialComboBoxOnAction(ActionEvent actionEvent) {
        selectingMaterial = null;
        for (Material material : materials) {
            if (material.getName().equals(selectMaterialComboBox.getSelectionModel().getSelectedItem())) {
                selectingMaterial = material;
                break;
            }
        }
        quantityTextField.setText("1");
    }

    public void deleteButtonOnAction(ActionEvent actionEvent) {
        orderingMaterial.remove(materialTableView.getSelectionModel().getSelectedItem());
    }
}
