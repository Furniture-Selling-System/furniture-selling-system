package org.furniture.controllers;

import java.util.TreeMap;

import org.furniture.models.Furniture;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class OrderHistoryController extends AbstractPageController {

    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private ComboBox<Integer> quarterComboBox;
    @FXML private TableView<Furniture> furnitureTableView;
    @FXML private TableColumn<Furniture, String> furnitureIdTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureNameTableColumn;
    @FXML private TableColumn<Furniture, String> furniturePriceTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureQuantityTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureAmountTableColumn;
    @FXML private TableColumn<Furniture, String> furnitureTotalPriceTableColumn;
    @FXML private TextField totalIncomeTextField;

    private ObservableList<Furniture> furnitureObservableList;
    private ObservableList<Integer> furnitureQuantityObservableList;

    @FXML
    private void comboBoxOnAction(MouseEvent e) {
        clear();

        int year    =   (yearComboBox.getValue() != null ? yearComboBox.getValue() : 0);
        int quarter =   (quarterComboBox.getValue() != null ? quarterComboBox.getValue() : 0);

        // TreeMap<Furniture, Integer> furnitureTreeMap = DBConnect.getFurnitureListByTime(int year, int quarter);
        // TODO: delete the following line of code
        TreeMap<Furniture, Integer> furnitureTreeMap = new TreeMap<>();
        
        for (Furniture f : furnitureTreeMap.keySet()) {
            furnitureObservableList.add(f);
            furnitureQuantityObservableList.add(furnitureTreeMap.get(f));
        }
    }

    @Override
    protected void initialize() {
        furnitureObservableList = FXCollections.observableArrayList();
        furnitureQuantityObservableList = FXCollections.observableArrayList();

        comboBoxOnAction(null);
        setUp();
    }

    private void setUp() {
        furnitureTableView.setItems(FXCollections.observableList(furnitureObservableList));
        furnitureIdTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getId());
            }
            
        });

        furnitureNameTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(arg0.getValue().getName());
            }
            
        });

        furniturePriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                return new SimpleStringProperty(String.valueOf(arg0.getValue().getPrice()));
            }
            
        });

        furnitureQuantityTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                // for (Furniture f : furnitureObservableList) {
                //     if (f == arg0.getValue()) {
                //     }
                // }
                // return null;
                for (int i = 0 ; i < furnitureObservableList.size() ; ++i) {
                    if (furnitureObservableList.get(i) == arg0.getValue()) {
                        return new SimpleStringProperty(String.valueOf(furnitureQuantityObservableList.get(i)));
                    }
                }
                return null;
            }
            
        });
        
        furnitureTotalPriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Furniture,String>,ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Furniture, String> arg0) {
                int quantity = 0;
                for (int i = 0 ; i < furnitureObservableList.size() ; ++i) {
                    if (furnitureObservableList.get(i) == arg0.getValue()) {
                        quantity = furnitureQuantityObservableList.get(i);
                        break;
                    }
                }
                return new SimpleStringProperty(String.valueOf(arg0.getValue().getPrice() * quantity));
            }
            
        });
    }
    

    private void clear() {
        furnitureObservableList.clear();
        furnitureQuantityObservableList.clear();
        totalIncomeTextField.clear();
    }

}
