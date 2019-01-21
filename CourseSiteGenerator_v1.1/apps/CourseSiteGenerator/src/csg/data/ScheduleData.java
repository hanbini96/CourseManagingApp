/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import static csg.CSGPropertyType.SCHEDULE_DATE_PICKER;
import static csg.CSGPropertyType.SCHEDULE_END_FRI_PICKER;
import static csg.CSGPropertyType.SCHEDULE_ITEMS_TABLE;
import static csg.CSGPropertyType.SCHEDULE_START_MON_PICKER;
import static csg.CSGPropertyType.SCHEDULE_TYPE_COMBOBOX;
import djf.modules.AppGUIModule;
import java.time.LocalDate;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

/**
 *
 * @author hanbin
 */
public class ScheduleData {
    CSGApp app;
    LocalDate startMon;
    LocalDate endFri;
    LocalDate scheduleDate;
    
    public enum ScheduleType {
        Holiday,
        Lecture,
        Reference,
        Recitation,
        HW
    }
    
    ObservableList<SchedulePrototype> schedules;
    ObservableList<String> types;
    
    
    public ScheduleData(CSGApp initApp){
        app = initApp;
        setupData();
    }
    
    private void setupData(){
        AppGUIModule gui = app.getGUIModule();
        
        DatePicker startDatePicker = (DatePicker)gui.getGUINode(SCHEDULE_START_MON_PICKER);
        startDatePicker.setOnAction(eh -> {
            startMon = startDatePicker.getValue();
        });
        DatePicker endDatePicker = (DatePicker)gui.getGUINode(SCHEDULE_END_FRI_PICKER);
        endDatePicker.setOnAction(eh -> {
            endFri = endDatePicker.getValue();
        });
        DatePicker scheduleDatePicker = (DatePicker)gui.getGUINode(SCHEDULE_DATE_PICKER);
        scheduleDatePicker.setOnAction(eh -> {
            scheduleDate = scheduleDatePicker.getValue();
        });
        
        types = ((ComboBox)gui.getGUINode(SCHEDULE_TYPE_COMBOBOX)).getItems();
        for (ScheduleType type: ScheduleType.values()){
            types.add(type.toString());
        }
        
        schedules = ((TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE)).getItems();
    }
    
    public void setStartMon(LocalDate startMon){
        this.startMon = startMon;
        ((DatePicker)app.getGUIModule().getGUINode(SCHEDULE_START_MON_PICKER)).setValue(startMon);
    }
    
    public void setEndFri(LocalDate endFri){
        this.endFri = endFri;
        ((DatePicker)app.getGUIModule().getGUINode(SCHEDULE_END_FRI_PICKER)).setValue(endFri);
    }
    
    public LocalDate getStartMon(){
        return startMon;
    }
    
    public LocalDate getEndFri(){
        return endFri;
    }
    
    public void addSchedule(SchedulePrototype schedule){
        schedules.add(schedule);
    }
    
    public void removeSchedule(SchedulePrototype schedule){
        schedules.remove(schedule);
    }
    
    public Iterator<SchedulePrototype> scheduleIterator() {
        return schedules.iterator();
    }

    public void reset() {
        startMon = LocalDate.now();
        endFri = LocalDate.now();
        scheduleDate = LocalDate.now();
        schedules.clear();
        types.clear();
        for (ScheduleType type: ScheduleType.values()){
            types.add(type.toString());
        }
    }
}
