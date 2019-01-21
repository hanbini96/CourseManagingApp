/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hanbin
 */
// includes recitation and lab
public class SubLecture extends Lecture{
    private final StringProperty ta1;
    private final StringProperty ta2;
    private final StringProperty dayTime;
    
    public SubLecture(){
        super();
        ta1 = new SimpleStringProperty("?");
        ta2 = new SimpleStringProperty("?");
        dayTime = new SimpleStringProperty("?");
    }
    
    public SubLecture(String initSection, String initDay, String initTime, String initRoom, String initTa1, String initTa2) {
        super(initSection, initDay, initTime, initRoom);
        ta1 = new SimpleStringProperty(initTa1);
        ta2 = new SimpleStringProperty(initTa2);
        dayTime = new SimpleStringProperty(initDay + ": " + initTime);
    }
    
    public StringProperty daytimeProperty() {
        return dayTime;
    }
    
    public void setDayTime(String dayTime){
        this.dayTime.setValue(dayTime);
    }
    
    public String getDayTime(){
        return dayTime.get();
    }
    
    public String getTa1() {
        return ta1.get();
    }
    
    public void setTa1(String initTa1) {
        ta1.setValue(initTa1);
    }
    
    public StringProperty ta1Property() {
        return ta1;
    }
    
    public String getTa2() {
        return ta2.get();
    }
    
    public void setTa2(String initTa2) {
        ta2.setValue(initTa2);
    }
    
    public StringProperty ta2Property() {
        return ta2;
    }
}
