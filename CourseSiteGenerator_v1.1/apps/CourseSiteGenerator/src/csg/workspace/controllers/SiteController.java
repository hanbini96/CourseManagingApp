package csg.workspace.controllers;

import djf.modules.AppGUIModule;
import csg.CSGApp;
import static csg.CSGPropertyType.SITE_EXPORTDIR;
import static csg.CSGPropertyType.SITE_NUMBER_COMBOBOX;
import static csg.CSGPropertyType.SITE_SEMESTER_COMBOBOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBOBOX;
import static csg.CSGPropertyType.SITE_YEAR_COMBOBOX;
import csg.data.CSGData;
import csg.data.SiteData;
import csg.data.SiteData.LogoType;
import csg.transactions.AddComboItem_Transaction;
import csg.transactions.ChangeCombo_Transaction;
import csg.transactions.ChangeTextArea_Transaction;
import csg.transactions.ChangeTextField_Transaction;
import csg.transactions.CheckBox_Transaction;
import csg.transactions.ImageUpdate_Transaction;
import csg.workspace.dialogs.FileSelecter;
import static djf.AppPropertyType.APP_PATH_EXPORT;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class SiteController {

    CSGApp app;

    public SiteController(CSGApp initApp) {
        app = initApp;
    }

    public void processComboAdd(ComboBox box) {
        if (!box.getItems().contains(box.getValue())){
            AddComboItem_Transaction addTransaction = new AddComboItem_Transaction(box,box.getValue());
            app.processTransaction(addTransaction);
        }
        updateDir();
    }
    
    public void updateDir(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        Label dir = (Label)gui.getGUINode(SITE_EXPORTDIR);
        if (subjectBox.getSelectionModel().getSelectedItem()!= null && numberBox.getSelectionModel().getSelectedItem() != "" && 
                semesterBox.getSelectionModel().getSelectedItem() != null && yearBox.getSelectionModel().getSelectedItem() != null){
            dir.setText(props.getProperty(APP_PATH_EXPORT) + subjectBox.getSelectionModel().getSelectedItem() + "_"
                + numberBox.getSelectionModel().getSelectedItem() + "_"
                + semesterBox.getSelectionModel().getSelectedItem() + "_"
                + yearBox.getSelectionModel().getSelectedItem() + "/index.html");
        }
        
    }

    public void processChangeTextField(TextField textField, String oldValue, String newValue) {
        ChangeTextField_Transaction transactoin = new ChangeTextField_Transaction(textField, oldValue, newValue);
        app.processTransaction(transactoin);
    }

    public void processChangeTextArea(TextArea textArea, String oldValue, String newValue) {
        ChangeTextArea_Transaction transactoin = new ChangeTextArea_Transaction(textArea, oldValue, newValue);
        app.processTransaction(transactoin);
    }

    public void processComboChange(ComboBox<String> subjectBox, String oldValue, String newValue) {
        ChangeCombo_Transaction transaction = new ChangeCombo_Transaction(subjectBox, oldValue, newValue);
        app.processTransaction(transaction);
    }

    public void processChangeImage(ImageView icon, LogoType logoType) {
        SiteData data = ((CSGData) app.getDataComponent()).getSiteData();
        String directory;
        File file = FileSelecter.showIconDialog(app.getGUIModule().getWindow());
        try{
            directory = file.getCanonicalPath();
        }
        catch (IOException e){
            directory = file.getAbsolutePath();
        }
        String base = System.getProperty("user.dir");
        String relative = "./" + new File(base).toURI().relativize(new File(directory).toURI()).getPath();
        ImageUpdate_Transaction transaction;
        if (logoType == LogoType.FAVICON){
            transaction = new ImageUpdate_Transaction(data, "", relative, logoType);            
        }
        else{
            transaction = new ImageUpdate_Transaction(data,relative, data.getHref(logoType), logoType);
        }
        
        app.processTransaction(transaction);
    }

    public void processCheck(CheckBox checkBox) {
        CheckBox_Transaction transaction = new CheckBox_Transaction(checkBox);
        app.processTransaction(transaction);
    }
}