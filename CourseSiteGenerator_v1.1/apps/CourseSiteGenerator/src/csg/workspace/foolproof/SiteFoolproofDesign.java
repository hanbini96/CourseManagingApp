/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.foolproof;

import csg.CSGApp;
import static csg.CSGPropertyType.SCHEDULE_DATE_PICKER;
import static csg.CSGPropertyType.SCHEDULE_END_FRI_PICKER;
import static csg.CSGPropertyType.SCHEDULE_START_MON_PICKER;
import static csg.CSGPropertyType.SITE_HOME_CHECKBOX;
import static csg.CSGPropertyType.SITE_HWS_CHECKBOX;
import static csg.CSGPropertyType.SITE_SCHEDULE_CHECKBOX;
import static csg.CSGPropertyType.SITE_SYLLABUS_CHECKBOX;
import djf.modules.AppGUIModule;
import djf.ui.foolproof.FoolproofDesign;
import java.time.LocalDate;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 *
 * @author hanbin
 */
public class SiteFoolproofDesign implements FoolproofDesign {

    CSGApp app;

    public SiteFoolproofDesign(CSGApp initApp) {
        app = initApp;
        
        
    }

    @Override
    public void updateControls() {
        AppGUIModule gui = app.getGUIModule();
        CheckBox cbHome = (CheckBox)gui.getGUINode(SITE_HOME_CHECKBOX);
        CheckBox cbSyllabus = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        CheckBox cbSchedule = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        CheckBox cbHWs = (CheckBox)gui.getGUINode(SITE_HWS_CHECKBOX);
        
        cbHome.disableProperty().bind(cbHome.selectedProperty()
                .and(cbSyllabus.selectedProperty().not())
                .and(cbSchedule.selectedProperty().not())
                .and(cbHWs.selectedProperty().not()));
        
        cbSyllabus.disableProperty().bind(cbHome.selectedProperty().not()
                .and(cbSyllabus.selectedProperty())
                .and(cbSchedule.selectedProperty().not())
                .and(cbHWs.selectedProperty().not()));
        
        cbSchedule.disableProperty().bind(cbHome.selectedProperty().not()
                .and(cbSyllabus.selectedProperty().not())
                .and(cbSchedule.selectedProperty())
                .and(cbHWs.selectedProperty().not()));
        
        cbHWs.disableProperty().bind(cbHome.selectedProperty().not()
                .and(cbSyllabus.selectedProperty().not())
                .and(cbSchedule.selectedProperty().not())
                .and(cbHWs.selectedProperty()));
    }
}