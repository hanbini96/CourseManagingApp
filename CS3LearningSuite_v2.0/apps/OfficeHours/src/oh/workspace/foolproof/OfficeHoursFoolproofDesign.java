package oh.workspace.foolproof;

import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_ADD_TA_BUTTON;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursFoolproofDesign implements FoolproofDesign {
    OfficeHoursApp app;
    
    public OfficeHoursFoolproofDesign(OfficeHoursApp initApp) {
        app = initApp;
    }

    @Override
    public void updateControls() {
        AppGUIModule gui = app.getGUIModule();
        
        TextField nameText = (TextField)gui.getGUINode(OH_NAME_TEXT_FIELD);
        BooleanBinding nameFieldFilled = Bindings.createBooleanBinding(() -> {
            return !nameText.getText().isEmpty();
        }, nameText.textProperty());
        
        TextField emailText = (TextField)gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        BooleanBinding emailFieldFilled = Bindings.createBooleanBinding(() -> {
            return !emailText.getText().isEmpty();
        }, emailText.textProperty());
        
        Button addButton = (Button) gui.getGUINode(OH_ADD_TA_BUTTON);
        addButton.disableProperty().bind(nameFieldFilled.not().or(emailFieldFilled.not()));
        
    }
}