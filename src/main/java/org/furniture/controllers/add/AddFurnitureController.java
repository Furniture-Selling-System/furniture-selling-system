package org.furniture.controllers.add;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.Page;
import org.furniture.models.Material;
import org.furniture.services.DBConnect;

import java.io.IOException;
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
    TableColumn<Material,String> materialNameTableColumn;

    @FXML
    TableColumn<Material,String> quantityTableColumn;

    @FXML
    ComboBox<Material> selectMaterialComboBox;

    @FXML
    TextField quantityTextField;

    @FXML
    Button deleteButton;

    private ObservableList<Material> data;
    private Material selectingMaterial;

    private ObservableList<Material> mList;

    @Override
    protected void initialize() {
        setUpClear();
        clear();
        getData();
        showMaterial();
    }

    private void showMaterial() {
        selectMaterialComboBox.setItems(data);
    }

    private void setUpClear() {
        deleteButton.setDisable(true);
    }

    private void clear() {
        furnitureIdTextField.clear();
        furnitureIdTextField.clear();
        furnitureIdTextField.clear();
        quantityTextField.clear();
        if (data != null) {
            data.clear();
        }
    }

    private void getData() {
        List<Material> materialsList = DBConnect.getMaterialsList();
        data = FXCollections.observableList(materialsList);
    }

    @FXML
    private void addButtonOnAction(ActionEvent e) throws IOException {
        // TODO: INSERT
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent e) throws IOException {
        UIManager.setPage(Page.CUSTOMERS_PAGE);
    }
}
