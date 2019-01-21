/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.CSGApp;
import static csg.CSGPropertyType.SYLL_AD_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_DESC_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_GRADE_COMP_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_GRADE_NOTE_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_OUTCOMES_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_PREREQ_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_SA_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_TEXTBOOKS_TEXT_AREA;
import static csg.CSGPropertyType.SYLL_TOPICS_TEXT_AREA;
import djf.modules.AppGUIModule;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;

/**
 *
 * @author hanbin
 */
public class SyllabusData {
    CSGApp app;
    private StringProperty description;
    private StringProperty topics;
    private StringProperty prerequisites;
    private StringProperty outcomes;
    private StringProperty textbooks;
    private StringProperty gradedComponents;
    private StringProperty gradingNote;
    private StringProperty academicDishonesty;
    private StringProperty specialAssistance;
    
    
    SyllabusData(CSGApp initApp) {
        app = initApp;
        setupData();
    }
    
    private void setupData(){
        AppGUIModule gui = app.getGUIModule();
        description = ((TextArea)gui.getGUINode(SYLL_DESC_TEXT_AREA)).textProperty();
        description.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        topics = ((TextArea)gui.getGUINode(SYLL_TOPICS_TEXT_AREA)).textProperty();
        topics.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        prerequisites = ((TextArea)gui.getGUINode(SYLL_PREREQ_TEXT_AREA)).textProperty();
        prerequisites.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        outcomes = ((TextArea)gui.getGUINode(SYLL_OUTCOMES_TEXT_AREA)).textProperty();
        outcomes.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        textbooks = ((TextArea)gui.getGUINode(SYLL_TEXTBOOKS_TEXT_AREA)).textProperty();
        textbooks.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        gradedComponents = ((TextArea)gui.getGUINode(SYLL_GRADE_COMP_TEXT_AREA)).textProperty();
        gradedComponents.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        gradingNote = ((TextArea)gui.getGUINode(SYLL_GRADE_NOTE_TEXT_AREA)).textProperty();
        gradingNote.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        academicDishonesty = ((TextArea)gui.getGUINode(SYLL_AD_TEXT_AREA)).textProperty();
        academicDishonesty.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
        specialAssistance = ((TextArea)gui.getGUINode(SYLL_SA_TEXT_AREA)).textProperty();
        specialAssistance.addListener(cl -> {
            app.getFileModule().markAsEdited(true);
        });
    }
    
    public void initData(String desc, String topic, String prereq, String outcome, String textbook, String gradingComp, String gradeNote, String academicDishonesty, String specialAssistance){
        this.setDescription(desc);
        this.setTopics(topic);
        this.setPrerequisites(prereq);
        this.setOutcomes(outcome);
        this.setTextbooks(textbook);
        this.setGradedComponents(gradingComp);
        this.setGradingNote(gradeNote);
        this.setAcademicDishonesty(academicDishonesty);
        this.setSpecialAssistance(specialAssistance);
    }
    
    public void setDescription(String description){
        this.description.set(description);
    }
    
    public String getDescription(){
        return this.description.get();
    }
    
    public void setTopics(String topics){
        this.topics.set(topics);
    }
    
    public String getTopics(){
        return this.topics.get();
    }
    
    public void setPrerequisites(String prerequisites){
        this.prerequisites.set(prerequisites);
    }
    
    public String getPrerequisites(){
        return this.prerequisites.get();
    }
    
    public void setOutcomes(String outcomes){
        this.outcomes.set(outcomes);
    }
    
    public String getOutcomes(){
        return this.outcomes.get();
    }
    
    public void setTextbooks(String textbooks){
        this.textbooks.set(textbooks);
    }
    
    public String getTextbooks(){
        return this.textbooks.get();
    }
    
    public void setGradedComponents(String gradedComponents){
        this.gradedComponents.set(gradedComponents);
    }
    
    public String getGradedComponents(){
        return this.gradedComponents.get();
    }
    
    public void setGradingNote(String gradingNote){
        this.gradingNote.set(gradingNote);
    }
    
    public String getGradingNote(){
        return this.gradingNote.get();
    }
    
    public void setAcademicDishonesty(String academicDishonesty){
        this.academicDishonesty.set(academicDishonesty);
    }
    
    public String getAcademicDishonesty(){
        return this.academicDishonesty.get();
    }
    
    public void setSpecialAssistance(String specialAssistance){
        this.specialAssistance.set(specialAssistance);
    }
    
    public String getSpecialAssistance(){
        return this.specialAssistance.get();
    }

    public void reset() {
        this.setDescription("");
        this.setTopics("");
        this.setPrerequisites("");
        this.setOutcomes("");
        this.setTextbooks("");
        this.setGradedComponents("");
        this.setGradingNote("");
        this.setAcademicDishonesty("");
        this.setSpecialAssistance("");
    }
}
