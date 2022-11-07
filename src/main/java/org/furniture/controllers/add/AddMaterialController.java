package org.furniture.controllers.add;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    public void confirmButtonOnAction(ActionEvent actionEvent) throws IOException {
        if(validation()) {
            Material material = new Material(idTextField.getText(),
                    nameTextField.getText(),
                    Integer.parseInt(quantityTextField.getText()),
                    Integer.parseInt(minimumTextField.getText()));
            DBConnect.insertMaterial(material);
            UIManager.setPage(Page.MATERIALS_PAGE);
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
        if (minimumTextField.getText().trim().isEmpty()){
            return false;
        }
        if(quantityTextField.getText().trim().isEmpty()){
            return false;
        }
        return isInt(quantityTextField.getText()) && isInt(minimumTextField.getText());
    }

    private boolean isInt(String str){
        return str.matches("\\d+");
    }
}
