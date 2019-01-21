/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import csg.CSGPropertyType;
import static csg.CSGPropertyType.SITE_EMAIL_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_FAV_ICON;
import static csg.CSGPropertyType.SITE_HOMEPAGE_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_HOME_CHECKBOX;
import static csg.CSGPropertyType.SITE_HWS_CHECKBOX;
import static csg.CSGPropertyType.SITE_LEFT_FOOT_ICON;
import static csg.CSGPropertyType.SITE_NAME_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_NAV_BAR_ICON;
import static csg.CSGPropertyType.SITE_NUMBER_COMBOBOX;
import static csg.CSGPropertyType.SITE_OFFICE_HOURS_TEXT_AREA;
import static csg.CSGPropertyType.SITE_RIGHT_FOOT_ICON;
import static csg.CSGPropertyType.SITE_ROOM_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_SCHEDULE_CHECKBOX;
import static csg.CSGPropertyType.SITE_SEMESTER_COMBOBOX;
import static csg.CSGPropertyType.SITE_STYLE_SHEET_COMBOBOX;
import static csg.CSGPropertyType.SITE_SUBJECT_COMBOBOX;
import static csg.CSGPropertyType.SITE_SYLLABUS_CHECKBOX;
import static csg.CSGPropertyType.SITE_TITLE_TEXT_FIELD;
import static csg.CSGPropertyType.SITE_YEAR_COMBOBOX;
import djf.modules.AppGUIModule;
import java.util.HashMap;
import java.util.Iterator;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 *
 * @author hanbin
 */
public class SiteData {
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;
    
    public enum LogoType{
        FAVICON, NAVBAR, BOTTOMLEFT, BOTTOMRIGHT
    }
    private HashMap<LogoType, String> srcs;
    private HashMap<LogoType, String> hrefs;;
    
    StringProperty title;
    ObservableList<String> cssSheets;
    StringProperty instructorName;
    StringProperty instructorRoom;
    StringProperty instructorEmail;
    StringProperty instructorHomepage;
    StringProperty instructorOfficeHours;
    String instructorPhoto = "";
    
    public SiteData(CSGApp initApp){
        app = initApp;
        dataSetup();
    }
    
    
    private void dataSetup(){
        AppGUIModule gui = app.getGUIModule();
        
        title = ((TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD)).textProperty();
        
        instructorName = ((TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD)).textProperty();
        instructorName.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        instructorRoom = ((TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD)).textProperty();
        instructorRoom.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        instructorEmail = ((TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD)).textProperty();
        instructorEmail.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        instructorHomepage = ((TextField)gui.getGUINode(SITE_HOMEPAGE_TEXT_FIELD)).textProperty();
        instructorEmail.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        instructorOfficeHours = ((TextArea)gui.getGUINode(SITE_OFFICE_HOURS_TEXT_AREA)).textProperty();
        instructorOfficeHours.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        
        srcs = new HashMap();
        hrefs = new HashMap();
        
        for (LogoType value : LogoType.values()){
            srcs.put(value, "");
        }
        
        for (LogoType value : LogoType.values()){
            hrefs.put(value, "");
        }
        
        ComboBox<String> cssSheetsBox = (ComboBox)gui.getGUINode(SITE_STYLE_SHEET_COMBOBOX);
        cssSheetsBox.setOnAction(eh->{
            app.getFileModule().markAsEdited(true);
        });
        cssSheets = cssSheetsBox.getItems();
        
    }
    
    public void setSiteInfo(String subject, String number, String semester, String year, String title){
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        subjectBox.getSelectionModel().select(subject);
        
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        numberBox.getSelectionModel().select(number);
        
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        semesterBox.getSelectionModel().select(semester);
        
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        yearBox.getSelectionModel().select(year);
        
        this.title.set(title);
    }
    
    public String getSubject(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        return subjectBox.getSelectionModel().getSelectedItem();
    }
    
    public String getNumber(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        return numberBox.getSelectionModel().getSelectedItem();
    }
    
    public String getSemester(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        return semesterBox.getSelectionModel().getSelectedItem();
    }
    
    public String getYear(){
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        return yearBox.getSelectionModel().getSelectedItem();
    }
    
    public String getTitle(){
        return title.get();
    }
    
    public void setLogoInfo(String src, LogoType type){
        srcs.replace(type, src);
    }
    
    public String getLogoInfo(LogoType type){
        return srcs.get(type);
    }
    
    public void setHref(String href, LogoType type){
        hrefs.replace(type, href);
    }
    
    public String getHref(LogoType type){
        return hrefs.get(type);
    }
    
    public void updateLogos(){
        AppGUIModule gui = app.getGUIModule();
        ImageView favIcon = (ImageView)gui.getGUINode(SITE_FAV_ICON);
        favIcon.setImage(null);
        gui.getNodesBuilder().setIcon(favIcon, getHref(LogoType.FAVICON));
        
        ImageView navIcon = (ImageView)gui.getGUINode(SITE_NAV_BAR_ICON);
        gui.getNodesBuilder().setIcon(navIcon, getLogoInfo(LogoType.NAVBAR));
        
        ImageView lfIcon = (ImageView)gui.getGUINode(SITE_LEFT_FOOT_ICON);
        gui.getNodesBuilder().setIcon(lfIcon, getLogoInfo(LogoType.BOTTOMLEFT));
        
        ImageView rfIcon = (ImageView)gui.getGUINode(SITE_RIGHT_FOOT_ICON);
        gui.getNodesBuilder().setIcon(rfIcon, getLogoInfo(LogoType.BOTTOMRIGHT));
    }
    
    public void setInstructorInfo(String name, String link, String email, String room, String photo, String hours){
        this.instructorName.set(name);
        instructorRoom.set(room);
        instructorEmail.set(email);
        instructorHomepage.set(link);
        instructorPhoto = photo;
        instructorOfficeHours.set(hours);
    }
    
    public void togglePage(String pageName){
        AppGUIModule gui = app.getGUIModule();
        ((CheckBox)gui.getGUINode(CSGPropertyType.valueOf("SITE_"+pageName.toUpperCase()+"_CHECKBOX"))).setSelected(true);
    }
    
    public boolean isPage(String pageName){
        AppGUIModule gui = app.getGUIModule();
        return ((CheckBox)gui.getGUINode(CSGPropertyType.valueOf("SITE_"+pageName.toUpperCase()+"_CHECKBOX"))).isSelected();
    }
    
    
    // accessors
    public void setInstructorName(String name){
        instructorName.set(name);
    }
    
    public String getInstructorName(){
        return instructorName.get();
    }
    
    public void setInstructorRoom(String room){
        instructorRoom.set(room);
    }
    
    public String getInstructorRoom(){
        return instructorRoom.get();
    }
    
    public void setInstructorEmail(String email){
        instructorEmail.set(email);
    }
    
    public String getInstructorEmail(){
        return instructorEmail.get();
    }
    
    public void setInstructorHomepage(String homepage){
        instructorHomepage.set(homepage);
    }
    
    public String getInstructorHomepage(){
        return instructorHomepage.get();
    }
    
    public void setInstructorOfficeHours(String officeHours){
        instructorOfficeHours.set(officeHours);
    }
    
    public String getInstructorOfficeHours(){
        return instructorOfficeHours.get();
    }
    
    public String getInstructorPhoto(){
        return instructorPhoto;
    }
    
    
    public Iterator<String> cssSheetsIterator() {
        return cssSheets.iterator();
    }

    public void reset() {
        AppGUIModule gui = app.getGUIModule();
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        
        subjectBox.getSelectionModel().clearSelection();
        numberBox.getSelectionModel().clearSelection();
        semesterBox.getSelectionModel().clearSelection();
        yearBox.getSelectionModel().clearSelection();
        
        ((CheckBox)gui.getGUINode(SITE_HOME_CHECKBOX)).setSelected(true);
        ((CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECKBOX)).setSelected(false);
        ((CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECKBOX)).setSelected(false);
        ((CheckBox)gui.getGUINode(SITE_HWS_CHECKBOX)).setSelected(false);
    }
}
