package oh.workspace.controllers;

import djf.modules.AppGUIModule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import oh.OfficeHoursApp;
import static oh.OfficeHoursPropertyType.OH_NAME_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.OfficeHoursPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.transactions.AddTA_Transaction;
import oh.transactions.ClickOH_Transaction;
import oh.workspace.dialogs.OfficeHoursDialog;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursController {

    OfficeHoursApp app;
    
    // from https://stackoverflow.com/questions/8204680/java-regex-email
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public OfficeHoursController(OfficeHoursApp initApp) {
        app = initApp;
    }

    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String name = nameTF.getText();
        String email = emailTF.getText();
        OfficeHoursData data = (OfficeHoursData) app.getDataComponent();
        // verify if the email format is correct and TA does not already exist
        boolean isValidEmail = verifyEmailAddress(email);
        boolean isUniqueName = nameIsUnique(name, data);
        boolean isUniqueEmail = emailIsUnique(email, data);
        if (isValidEmail && isUniqueName && isUniqueEmail) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);
        }
        else {
            new OfficeHoursDialog(app).showAddTAButtonError(name, email, isUniqueName, isUniqueEmail, isValidEmail);
        }

        // NOW CLEAR THE TEXT FIELDS
        nameTF.setText("");
        nameTF.requestFocus();
        
        emailTF.setText("");
        emailTF.requestFocus();
    }
    
    public void processClickOH(TeachingAssistantPrototype ta) {
        AppGUIModule gui = app.getGUIModule();
        TableView ohTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        OfficeHoursData data = (OfficeHoursData) app.getDataComponent();
        int col = data.getSelectedColumn();
        if (data.isDayOfWeekColumn(col)){
            TimeSlot slot = data.getSelectedSlot();
            ClickOH_Transaction clickOHTansaction = new ClickOH_Transaction(ohTableView, data, ta, slot, col);
            app.processTransaction(clickOHTansaction);
        }
    }
    
    private boolean verifyEmailAddress(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
    private boolean nameIsUnique(String name, OfficeHoursData data){
        return data.getTAWithName(name) == null;
    }
    
    private boolean emailIsUnique(String email, OfficeHoursData data){
        return data.getTAWithEmail(email) == null;
    }
}
