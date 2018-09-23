/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oh.workspace.dialogs;

import static djf.AppPropertyType.NEW_ERROR_TITLE;
import djf.AppTemplate;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import static oh.OfficeHoursPropertyType.*;
import properties_manager.PropertiesManager;

/**
 *
 * @author hanbin
 */
public class OfficeHoursDialog extends Stage{
    PropertiesManager props;
    public OfficeHoursDialog(AppTemplate app) {
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void showAddTAButtonError(String name, String email, boolean nameIsUnique, boolean emailIsUnique, boolean emailIsValid){
        String errorMsg = "";
        if (!emailIsValid){
            errorMsg += email + " " + props.getProperty(OH_INVALID_EMAIL_FORMAT_MESSAGE) + "\n";
        }
        if (!nameIsUnique){
            errorMsg += name + " " + props.getProperty(OH_NOT_UNIQUE_NAME_MESSAGE) + "\n";
        }
        
        if (!emailIsUnique){
            errorMsg += email + " " + props.getProperty(OH_NOT_UNIQUE_EMAIL_MESSAGE) + "\n";
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
            
            alert.setTitle(props.getProperty(NEW_ERROR_TITLE));
            alert.setHeaderText(props.getProperty(OH_CANNOT_ADD_TA_TITLE_LABEL));
            alert.setContentText(errorMsg);
            alert.setResizable(true);
            alert.showAndWait();
    }
}
