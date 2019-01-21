/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.controllers;

import csg.CSGApp;
import static csg.CSGPropertyType.SCHEDULE_DATE_PICKER;
import static csg.CSGPropertyType.SCHEDULE_ITEMS_TABLE;
import static csg.CSGPropertyType.SCHEDULE_LINK_TEXT_FIELD;
import static csg.CSGPropertyType.SCHEDULE_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SCHEDULE_TOPIC_TEXT_FIELD;
import static csg.CSGPropertyType.SCHEDULE_TYPE_COMBOBOX;
import csg.data.CSGData;
import csg.data.ScheduleData;
import csg.data.ScheduleData.ScheduleType;
import csg.data.SchedulePrototype;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.ChangeCombo_Transaction;
import csg.transactions.ChangeDate_Transaction;
import csg.transactions.ChangeTextField_Transaction;
import csg.transactions.EditSchedule_Transaction;
import csg.transactions.RemoveSchedule_Transaction;
import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author hanbin
 */
public class ScheduleController {
    CSGApp app;

    public ScheduleController(CSGApp initApp) {
        app = initApp;
    }

    public void processChangeDate(DatePicker dp, LocalDate oldValue, LocalDate newValue) {
        ChangeDate_Transaction dateTransaction = new ChangeDate_Transaction(dp, oldValue, newValue);
        app.processTransaction(dateTransaction);
    }

    public void processChangeType(ComboBox<String> subjectBox, String oldValue, String newValue) {
        ChangeCombo_Transaction transaction = new ChangeCombo_Transaction(subjectBox, oldValue, newValue);
        app.processTransaction(transaction);
    }

    public void processChangeTextField(TextField field, String oldValue, String newValue) {
        ChangeTextField_Transaction transaction = new ChangeTextField_Transaction(field, oldValue, newValue);
        app.processTransaction(transaction);
    }

    public void processAddUpdateSchedule() {
        ComboBox<String> subjectBox = (ComboBox)app.getGUIModule().getGUINode(SCHEDULE_TYPE_COMBOBOX);
        DatePicker dpDate = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_DATE_PICKER);
        TextField title = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TITLE_TEXT_FIELD);
        TextField topic = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TOPIC_TEXT_FIELD);
        TextField link = (TextField)app.getGUIModule().getGUINode(SCHEDULE_LINK_TEXT_FIELD);
        
        TableView<SchedulePrototype> tblSchedule = (TableView)app.getGUIModule().getGUINode(SCHEDULE_ITEMS_TABLE);
        if (!tblSchedule.getSelectionModel().getSelectedItems().isEmpty()){
            SchedulePrototype schedule = tblSchedule.getSelectionModel().getSelectedItem();
            EditSchedule_Transaction transaction = 
                    new EditSchedule_Transaction(schedule, dpDate.getValue(), title.getText(),
                            topic.getText(), link.getText(), subjectBox.getSelectionModel().getSelectedItem());
            app.processTransaction(transaction);
        }
        else{
            SchedulePrototype newSchedule = new SchedulePrototype(
                    dpDate.getValue(), title.getText(),
                    topic.getText(), ScheduleType.valueOf(subjectBox.getSelectionModel().getSelectedItem()),
                    link.getText()
            );
            ScheduleData data = ((CSGData)app.getDataComponent()).getScheduleData();
            AddSchedule_Transaction transaction = new AddSchedule_Transaction(data, newSchedule);
            app.processTransaction(transaction);
        }
        tblSchedule.refresh();
    }

    public void processClearSchedule() {
        TableView<SchedulePrototype> tblSchedule = (TableView)app.getGUIModule().getGUINode(SCHEDULE_ITEMS_TABLE);
        tblSchedule.getSelectionModel().clearSelection();
        
        ComboBox<String> subjectBox = (ComboBox)app.getGUIModule().getGUINode(SCHEDULE_TYPE_COMBOBOX);
        subjectBox.getSelectionModel().clearSelection();
        DatePicker dpDate = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_DATE_PICKER);
        dpDate.setValue(LocalDate.now());
        TextField title = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TITLE_TEXT_FIELD);
        title.clear();
        TextField topic = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TOPIC_TEXT_FIELD);
        topic.clear();
        TextField link = (TextField)app.getGUIModule().getGUINode(SCHEDULE_LINK_TEXT_FIELD);
        link.clear();
        
    }

    public void processRemoveSchedule() {
        TableView<SchedulePrototype> tblSchedule = (TableView)app.getGUIModule().getGUINode(SCHEDULE_ITEMS_TABLE);
        ScheduleData data = ((CSGData)app.getDataComponent()).getScheduleData();
        SchedulePrototype schedule = tblSchedule.getSelectionModel().getSelectedItem();
        
        RemoveSchedule_Transaction transaction = new RemoveSchedule_Transaction(data, schedule);
        app.processTransaction(transaction);
        
        tblSchedule.refresh();
    }

    public void processToggleSchedule() {
        TableView<SchedulePrototype> tblSchedule = (TableView)app.getGUIModule().getGUINode(SCHEDULE_ITEMS_TABLE);
        
        if (!tblSchedule.getSelectionModel().getSelectedItems().isEmpty()){
       
            ComboBox<String> subjectBox = (ComboBox)app.getGUIModule().getGUINode(SCHEDULE_TYPE_COMBOBOX);
            DatePicker dpDate = (DatePicker)app.getGUIModule().getGUINode(SCHEDULE_DATE_PICKER);
            TextField title = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TITLE_TEXT_FIELD);
            TextField topic = (TextField)app.getGUIModule().getGUINode(SCHEDULE_TOPIC_TEXT_FIELD);
            TextField link = (TextField)app.getGUIModule().getGUINode(SCHEDULE_LINK_TEXT_FIELD);

            SchedulePrototype schedule = tblSchedule.getSelectionModel().getSelectedItem();

            subjectBox.getSelectionModel().select(schedule.getType());
            dpDate.setValue(schedule.getDate());
            title.setText(schedule.getTitle());
            topic.setText(schedule.getTopic());
            link.setText(schedule.getLink());
        }
    }
}
