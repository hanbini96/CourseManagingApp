package oh.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class stores information for a single row in our
 * office hours table.
 * 
 * @author Richard McKenna
 */
public class TimeSlot {

    public enum DayOfWeek {   
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY
    }   
    private StringProperty startTime;
    private StringProperty endTime;
    private HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> tas;
    private HashMap<DayOfWeek, StringProperty> dayText;
    

    public TimeSlot(String initStartTime, String initEndTime) {
        startTime = new SimpleStringProperty(initStartTime);
        endTime = new SimpleStringProperty(initEndTime);
        tas = new HashMap();
        dayText = new HashMap();
        for (DayOfWeek dow : DayOfWeek.values()) {
            tas.put(dow, new ArrayList());
            dayText.put(dow, new SimpleStringProperty());
        }
    }

    // ACCESSORS AND MUTATORS
    public boolean addTA(String day, TeachingAssistantPrototype ta){
        if (tas.get(DayOfWeek.valueOf(day)).contains(ta)){
            return false;
        }
        tas.get(DayOfWeek.valueOf(day)).add(ta);
        dayText.get(DayOfWeek.valueOf(day)).setValue(
                tas.get(DayOfWeek.valueOf(day))
                        .stream()
                        .map(a -> String.valueOf(a.getName()))
                        .collect(Collectors.joining("\n")));
        ta.setSlot(ta.getSlot()+1);
        return true;
    }
    
    public boolean addTA(DayOfWeek dow, TeachingAssistantPrototype ta){
        if (tas.get(dow).contains(ta)){
            return false;
        }
        tas.get(dow).add(ta);
        dayText.get(dow).setValue(
                tas.get(dow)
                        .stream()
                        .map(a -> String.valueOf(a.getName()))
                        .collect(Collectors.joining("\n")));
        ta.setSlot(ta.getSlot()+1);
        return true;
    }
    
    public boolean removeTA(String day, TeachingAssistantPrototype ta){
        if (!tas.get(DayOfWeek.valueOf(day)).contains(ta)){
            return false;
        }
        tas.get(DayOfWeek.valueOf(day)).remove(ta);
        dayText.get(DayOfWeek.valueOf(day)).setValue(
                tas.get(DayOfWeek.valueOf(day))
                        .stream()
                        .map(a -> String.valueOf(a.getName()))
                        .collect(Collectors.joining("\n")));
        ta.setSlot(ta.getSlot()-1);
        return true;
    }
    
    public boolean removeTA(DayOfWeek dow, TeachingAssistantPrototype ta){
        if (!tas.get(dow).contains(ta)){
            return false;
        }
        tas.get(dow).remove(ta);
        dayText.get(dow).setValue(
                tas.get(dow)
                        .stream()
                        .map(a -> String.valueOf(a.getName()))
                        .collect(Collectors.joining("\n")));
        ta.setSlot(ta.getSlot()-1);
        return true;
    }
    
    
    
    public HashMap<DayOfWeek, ArrayList<TeachingAssistantPrototype>> getTA(){
        return tas;
    }
    

    public String getStartTime() {
        return startTime.getValue();
    }
    
    public void setStartTime(String initStartTime) {
        startTime.setValue(initStartTime);
    }
    
    public StringProperty startTimeProperty() {
        return startTime;
    }
    
    public String getEndTime() {
        return endTime.getValue();
    }
    
    public void setEndTime(String initEndTime) {
        endTime.setValue(initEndTime);
    }
    
    public StringProperty endTimeProperty() {
        return endTime;
    }
    
    public String getMonday() {
        return dayText.get(DayOfWeek.MONDAY).getValue();
    }
    
    public void setMonday(String initMonday) {
        dayText.get(DayOfWeek.MONDAY).setValue(initMonday);
    }
    
    public StringProperty mondayProperty() {
        return this.dayText.get(DayOfWeek.MONDAY);
    }
    
    public String getTuesday() {
        return dayText.get(DayOfWeek.MONDAY).getValue();
    }
    
    public void setTuesday(String initTuesday) {
        dayText.get(DayOfWeek.MONDAY).setValue(initTuesday);
    }
    
    public StringProperty tuesdayProperty() {
        return this.dayText.get(DayOfWeek.TUESDAY);
    }
    
    public String getWednesday() {
        return dayText.get(DayOfWeek.MONDAY).getValue();
    }
    
    public void setWednesday(String initWednesday) {
        dayText.get(DayOfWeek.MONDAY).setValue(initWednesday);
    }
    
    public StringProperty wednesdayProperty() {
        return this.dayText.get(DayOfWeek.WEDNESDAY);
    }
    
    public String getThursday() {
        return dayText.get(DayOfWeek.MONDAY).getValue();
    }
    
    public void setThursday(String initThursday) {
        dayText.get(DayOfWeek.MONDAY).setValue(initThursday);
    }
    
    public StringProperty thursdayProperty() {
        return this.dayText.get(DayOfWeek.THURSDAY);
    }
    
    public String getFriday() {
        return dayText.get(DayOfWeek.MONDAY).getValue();
    }
    
    public void setFriday(String initFriday) {
        dayText.get(DayOfWeek.MONDAY).setValue(initFriday);
    }
    
    public StringProperty fridayProperty() {
        return this.dayText.get(DayOfWeek.FRIDAY);
    }
    
    public void reset() {
        for (DayOfWeek dow : DayOfWeek.values()) {
            tas.get(dow).clear();
            dayText.get(dow).setValue("");
        }
    }
}