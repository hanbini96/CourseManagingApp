/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.dialogs;

import static csg.CSGPropertyType.ICON_FILE_EXT;
import static csg.CSGPropertyType.ICON_FILE_EXT_DESC;
import static csg.CSGPropertyType.SITE_ICON_SELECT_DIALOG;
import static djf.AppTemplate.PATH_ICONS;
import static djf.AppTemplate.PATH_WORK;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author hanbin
 */
public class FileSelecter {
    public static File showIconDialog(Stage window){
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_ICONS + "logos/"));
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        fc.setTitle(props.getProperty(SITE_ICON_SELECT_DIALOG));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(props.getProperty(ICON_FILE_EXT_DESC), props.getProperty(ICON_FILE_EXT))
        );
        File selectedFile = fc.showOpenDialog(window);
        return selectedFile;
    }
    
    public static ObservableList getStyleSheets(){
        List<String> list = new ArrayList<String>();
        ObservableList<String> fileList = FXCollections.observableList(list);
        File repo = new File (PATH_WORK + "css");
        for (File f : repo.listFiles()){
            fileList.add(f.getName());
        }
        
        return fileList;
    }
}
