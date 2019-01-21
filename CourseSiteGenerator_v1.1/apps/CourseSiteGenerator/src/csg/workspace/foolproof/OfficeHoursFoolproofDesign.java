package csg.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import csg.CSGApp;
import static csg.CSGPropertyType.OH_END_TIME_COMBO_BOX;
import static csg.CSGPropertyType.OH_START_TIME_COMBO_BOX;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import csg.data.CSGData;
import csg.data.OfficeHoursData;
import static csg.workspace.style.CSGStyle.CLASS_CSG_TEXT_FIELD;
import static csg.workspace.style.CSGStyle.CLASS_CSG_TEXT_FIELD_ERROR;
import javafx.scene.control.ComboBox;

public class OfficeHoursFoolproofDesign implements FoolproofDesign {

    CSGApp app;

    public OfficeHoursFoolproofDesign(CSGApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        updateAddTAFoolproofDesign();
        updateEditTAFoolproofDesign();
        updateSelectTimeRangeFoolproofDesign();
    }

    private void updateAddTAFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        
        // FOOLPROOF DESIGN STUFF FOR ADD TA BUTTON
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        Button addTAButton = (Button) gui.getGUINode(OH_ADD_TA_BUTTON);

        // FIRST, IF NO TYPE IS SELECTED WE'LL JUST DISABLE
        // THE CONTROLS AND BE DONE WITH IT
        boolean isTypeSelected = data.isTATypeSelected();
        if (!isTypeSelected) {
            nameTextField.setDisable(true);
            emailTextField.setDisable(true);
            addTAButton.setDisable(true);
            return;
        } // A TYPE IS SELECTED SO WE'LL CONTINUE
        else {
            nameTextField.setDisable(false);
            emailTextField.setDisable(false);
            addTAButton.setDisable(false);
        }

        // NOW, IS THE USER-ENTERED DATA GOOD?
        boolean isLegalNewTA = data.isLegalNewTA(name, email);

        // ENABLE/DISABLE THE CONTROLS APPROPRIATELY
        addTAButton.setDisable(!isLegalNewTA);
        if (isLegalNewTA) {
            nameTextField.setOnAction(addTAButton.getOnAction());
            emailTextField.setOnAction(addTAButton.getOnAction());
        } else {
            nameTextField.setOnAction(null);
            emailTextField.setOnAction(null);
        }

        // UPDATE THE CONTROL TEXT DISPLAY APPROPRIATELY
        boolean isLegalNewName = data.isLegalNewName(name);
        boolean isLegalNewEmail = data.isLegalNewEmail(email);
        foolproofTextField(nameTextField, isLegalNewName);
        foolproofTextField(emailTextField, isLegalNewEmail);
    }
    
    private void updateEditTAFoolproofDesign() {
        
    }
    
    public void foolproofTextField(TextField textField, boolean hasLegalData) {
        if (hasLegalData) {
            textField.getStyleClass().remove(CLASS_CSG_TEXT_FIELD_ERROR);
            if (!textField.getStyleClass().contains(CLASS_CSG_TEXT_FIELD)) {
                textField.getStyleClass().add(CLASS_CSG_TEXT_FIELD);
            }
        } else {
            textField.getStyleClass().remove(CLASS_CSG_TEXT_FIELD);
            if (!textField.getStyleClass().contains(CLASS_CSG_TEXT_FIELD_ERROR)) {
                textField.getStyleClass().add(CLASS_CSG_TEXT_FIELD_ERROR);
            }
        }
    }
    
    private void updateSelectTimeRangeFoolproofDesign() {
        
        AppGUIModule gui = app.getGUIModule();
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        
        ComboBox<String> startHoursBox = (ComboBox)gui.getGUINode(OH_START_TIME_COMBO_BOX);
        
        ComboBox<String> endHoursBox = (ComboBox)gui.getGUINode(OH_END_TIME_COMBO_BOX);
        
        String current = endHoursBox.getSelectionModel().getSelectedItem();
        if (data.setEndHourList(startHoursBox.getSelectionModel().getSelectedItem(), current)){
            endHoursBox.getSelectionModel().select(current);
        }
        
        current = startHoursBox.getSelectionModel().getSelectedItem();
        if (data.setStartHourList(endHoursBox.getSelectionModel().getSelectedItem(), current)){
            startHoursBox.getSelectionModel().select(current);
        }
    }
}
