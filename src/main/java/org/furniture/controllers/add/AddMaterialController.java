package org.furniture.controllers.add;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.furniture.UIManager;
import org.furniture.controllers.AbstractPageController;
import org.furniture.enums.Page;
import org.furniture.models.Material;
import org.furniture.services.DBConnect;

import java.io.IOException;

public class AddMaterialController extends AbstractPageController {
    @FXML
    TextField idTextField;

    @FXML
    TextField nameTextField;

    @FXML
    TextField quantityTextField;

    @FXML
    TextField minimumTextField;

    @Override
    protected void initialize() {
        idTextField.setText(String.valueOf((DBConnect.getMaterialLastID()+1)));
    }

    public void confirmButtonOnAction(ActionEvent actionEvent){
        if(validation()) {
            try {
                Material material = new Material(idTextField.getText(),
                        nameTextField.getText(),
                        Integer.parseInt(quantityTextField.getText()),
                        Integer.parseInt(minimumTextField.getText()));
                DBConnect.insertMaterial(material);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Material has been successfully added.");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
                UIManager.setPage(Page.MATERIALS_PAGE);
            } catch (NumberFormatException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Material can't be created.");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }


    @FXML
    public void cancelButtonOnAction(ActionEvent actionEvent) throws IOException {
        UIManager.setPage(Page.MATERIALS_PAGE);
    }

    private boolean validation(){
        if(nameTextField.getText().trim().isEmpty()){
            return false;
        }
        if (minimumTextField.getText().trim().isEmpty() || minimumTextField.getLength() > 3){
            return false;
        }
        if(quantityTextField.getText().trim().isEmpty() || quantityTextField.getLength() > 4){
            return false;
        }
        return isInt(quantityTextField.getText()) && isInt(minimumTextField.getText());
    }

    private boolean isInt(String str){
        return str.matches("\\d+");
    }
}
