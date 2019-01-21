/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import static csg.CSGPropertyType.MT_REC_TABLE_VIEW;
import djf.modules.AppGUIModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author hanbin
 */
public class MTData {
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;
    
    ObservableList<Lecture> lectures;
    ObservableList<SubLecture> recitations;
    ObservableList<SubLecture> labs;
    HashMap<String, String> days;
    
    public MTData(CSGApp initApp){
        app = initApp;
        setupData();
    }
    
    private void setupData(){
        AppGUIModule gui = app.getGUIModule();
        
        TableView<Lecture> lectureTableView = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        lectures = lectureTableView.getItems();
        
        TableView<SubLecture> recTableView = (TableView)gui.getGUINode(MT_REC_TABLE_VIEW);
        recitations = recTableView.getItems();
        
        TableView<SubLecture> labTableView = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        labs = labTableView.getItems();
        
        days = new HashMap();
        days.put("Mondays", "M");
        days.put("Tuesdays", "Tu");
        days.put("Wednesdays", "W");
        days.put("Thursday", "Th");
        days.put("Fridays", "F");
    }
    
    // strip <strong> section </strong> (Professor) to section
    public String stripSection(String section){
        return section.split(">|</")[1];
    }
    
    public String wrapSection(String section, String instructorName){
        section = "<strong>" + section + "</strong> " + "(" + instructorName.split(" ")[1] + ")";
        return section;
    }
    
    public String extractDay(String dayTime){
        String extracted = "";
        for (String dow : days.keySet()){
            if (dayTime.contains(dow)){
                extracted += days.get(dow);
            }
        }
        return extracted;
    }
    
    public String getDayTime(SubLecture lecture){
        String days = getDays(lecture.getDay());
        String time = lecture.getTime();
        return days + ", " + time;
    }
    
    public String getDays(String dayString){
        ArrayList<String> dayList = new ArrayList();
        for (String key: days.keySet()){
            if (dayString.contains(days.get(key))){
                dayList.add(key);
            }
        }
        
        return String.join(" &amp; ", dayList);
    }
    
    public String extractTime(String dayTime){
        String[] splited = dayTime.split(" ");
        return splited[splited.length-1];
    }
    
    public void addLecture(Lecture lecture){
        lectures.add(lecture);
    }
    
    
    public void removeLecture(Lecture lecture) {
        lectures.remove(lecture);
    }
    
    public void addRecitation(SubLecture recitation){
        recitations.add(recitation);
    }
    
    public void removeRecitation(SubLecture recitation){
        recitations.remove(recitation);
    }
    
    public void addLab(SubLecture lab){
        labs.add(lab);
    }
    
    public void removeLab(SubLecture lab){
        labs.remove(lab);
    }
    
    public Iterator<Lecture> lecturesIterator() {
        return lectures.iterator();
    }
    
    public Iterator<SubLecture> recIterator() {
        return recitations.iterator();
    }
    
    public Iterator<SubLecture> labIterator() {
        return labs.iterator();
    }

    public void reset() {
        lectures.clear();
        recitations.clear();
        labs.clear();
    }

}
