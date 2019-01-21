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
import djf.ui.foolproof.FoolproofDesign;
import java.time.LocalDate;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

/**
 *
 * @author hanbin
 */
public class ScheduleFoolproofDesign implements FoolproofDesign {

    CSGApp app;

    public ScheduleFoolproofDesign(CSGApp initApp) {
        app = initApp;
        
        
        DatePicker dpStartMon = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_START_MON_PICKER);
        DatePicker dpEndFri = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_END_FRI_PICKER);
        DatePicker dpDate = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_DATE_PICKER);
        
        final Callback<DatePicker, DateCell> startCellFactory;

        startCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (dpEndFri.getValue() != null && item.isAfter(dpEndFri.getValue())) { //Disable all dates after required date
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
                }
            }
        };
        dpStartMon.setDayCellFactory(startCellFactory);
        
        final Callback<DatePicker, DateCell> endCellFactory;

        endCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (dpStartMon.getValue() != null && item.isBefore(dpStartMon.getValue())) { //Disable all dates after required date
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
                }
            }
        };
        dpEndFri.setDayCellFactory(endCellFactory);
        
        final Callback<DatePicker, DateCell> dayCellFactory;

        dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (dpEndFri.getValue() != null && item.isAfter(dpEndFri.getValue())) { //Disable all dates after required date
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
                }
                if (dpStartMon.getValue() != null && item.isBefore(dpStartMon.getValue())) { //Disable all dates after required date
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color
                }
            }
        };
        //Finally, we just need to update our DatePicker cell factory as follow:
        dpDate.setDayCellFactory(dayCellFactory);
    }

    @Override
    public void updateControls() {
    }
}