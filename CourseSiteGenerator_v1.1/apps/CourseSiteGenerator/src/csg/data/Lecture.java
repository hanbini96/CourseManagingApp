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
public class Lecture {
    protected final StringProperty section;
    protected final StringProperty day;
    protected final StringProperty time;
    protected final StringProperty room;
    
    public Lecture(){
        section = new SimpleStringProperty("?");
        day = new SimpleStringProperty("?");
        time = new SimpleStringProperty("?");
        room = new SimpleStringProperty("?");
    }
    
    public Lecture(String initSection, String initDay, String initTime, String initRoom) {
        section = new SimpleStringProperty(initSection);
        day = new SimpleStringProperty(initDay);
        time = new SimpleStringProperty(initTime);
        room = new SimpleStringProperty(initRoom);
    }
    
    public String getSection() {
        return section.get();
    }
    
    public void setSection(String initSection) {
        section.setValue(initSection);
    }
    
    public StringProperty sectionProperty() {
        return section;
    }
    
    public String getDay() {
        return day.get();
    }
    
    public void setDay(String initDay) {
        day.setValue(initDay);
    }
    
    public StringProperty dayProperty() {
        return day;
    }
    
    public String getTime() {
        return time.get();
    }
    
    public void setTime(String initTime) {
        time.setValue(initTime);
    }
    
    public StringProperty timeProperty() {
        return time;
    }
    
    public String getRoom() {
        return room.get();
    }
    
    public void setRoom(String initRoom) {
        room.setValue(initRoom);
    }
    
    public StringProperty roomProperty() {
        return room;
    }
}
