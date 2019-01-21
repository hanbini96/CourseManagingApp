/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import csg.data.ScheduleData.ScheduleType;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author hanbin
 */
public class SchedulePrototype {
    LocalDate date;
    private final StringProperty dateProperty;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty type;
    private String link;
    
    public SchedulePrototype(LocalDate initDate, String initTitle, String initTopic, ScheduleType initType, String initLink){
        date = initDate;
        dateProperty = new SimpleStringProperty(initDate.toString());
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        type = new SimpleStringProperty(initType.toString());
        link = initLink;
    }
    
    public LocalDate getDate(){
        return date;
    }
    
    public void setDate(LocalDate initDate){
        date = initDate;
    }
    
    public StringProperty dateProperty(){
        dateProperty.set(date.toString());
        return dateProperty;
    }
    
    public String getTitle(){
        return title.get();
    }
    
    public void setTitle(String initTitle){
        title.set(initTitle);
    }
    
    public StringProperty titleProperty(){
        return title;
    }
    
    public String getTopic(){
        return topic.get();
    }
    
    public void setTopic(String initTopic){
        topic.set(initTopic);
    }
    
    public StringProperty topicProperty(){
        return topic;
    }
    
    public String getType(){
        return type.get();
    }
    
    public void setType(String initType){
        type.set(initType);
    }
    
    public StringProperty typeProperty(){
        return type;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
}
