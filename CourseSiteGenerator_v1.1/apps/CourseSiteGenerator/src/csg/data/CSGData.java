/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import djf.components.AppDataComponent;

import csg.CSGApp;
import djf.modules.AppGUIModule;
import javafx.collections.ObservableList;
import static csg.CSGPropertyType.SITE_NUMBER_COMBOBOX;
import static csg.CSGPropertyType.SITE_SEMESTER_COMBOBOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBOBOX;
import static csg.CSGPropertyType.SITE_YEAR_COMBOBOX;
import csg.files.CSGFiles;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;

/**
 *
 * @author hanbin
 */
public class CSGData implements AppDataComponent {
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;
    
    SiteData siteData;
    SyllabusData syllabusData;
    MTData mtData;
    OfficeHoursData ohData;
    ScheduleData scheduleData;
    
    ObservableList<String> subjects;
    ObservableList<String> numbers;
    ObservableList<String> semesters;
    ObservableList<String> years;
    

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CSGData(CSGApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        AppGUIModule gui = app.getGUIModule();
        
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        subjects = subjectBox.getItems();
        
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        numbers = numberBox.getItems();
        
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        semesters = semesterBox.getItems();
        
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        years = yearBox.getItems();
        

        siteData = new SiteData(initApp);
        syllabusData = new SyllabusData(initApp);
        mtData = new MTData(initApp);
        ohData = new OfficeHoursData(initApp);
        scheduleData = new ScheduleData(initApp);
    }
    
    
    public void addSubject(String subject){
        subjects.add(subject);
    }
    
    public void addNumber(String number){
        numbers.add(number);
    }
    
    public void addSemester(String semester){
        semesters.add(semester);
    }
    
    public void addYear(String year){
        years.add(year);
    }
    
    public OfficeHoursData getOHData(){
        return ohData;
    }
    
    public SiteData getSiteData(){
        return siteData;
    }
    
    public SyllabusData getSyllabusData(){
        return syllabusData;
    }
    
    public MTData getMTData(){
        return mtData;
    }
    
    public ScheduleData getScheduleData(){
        return scheduleData;
    }
    
    
    public Iterator<String> subjectsIterator() {
        return subjects.iterator();
    }
    
    public Iterator<String> numbersIterator() {
        return numbers.iterator();
    }
    
    public Iterator<String> semestersIterator() {
        return semesters.iterator();
    }
    
    public Iterator<String> yearsIterator() {
        return years.iterator();
    }
    
    
    // METHODS TO OVERRIDE
        
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void reset() {
        subjects.clear();
        numbers.clear();
        semesters.clear();
        years.clear();
        try {
            ((CSGFiles)app.getFileComponent()).loadCSGData(this);
        } catch (IOException ex) {
            Logger.getLogger(CSGData.class.getName()).log(Level.SEVERE, null, ex);
        }
        siteData.reset();
        syllabusData.reset();
        mtData.reset();
        ohData.reset();
        scheduleData.reset();
    }
}
