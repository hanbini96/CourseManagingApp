/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.SchedulePrototype;
import java.time.LocalDate;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class EditSchedule_Transaction implements jTPS_Transaction{
    SchedulePrototype schedule;
    LocalDate oldDate;
    LocalDate newDate;
    String oldTitle;
    String newTitle;
    String oldTopic;
    String newTopic;
    String oldLink;
    String newLink;
    String oldType;
    String newType;
    
    public EditSchedule_Transaction(SchedulePrototype initSchedule, LocalDate initDate, 
            String initTitle, String initTopic, String initLink, String initType){
        schedule = initSchedule;
        oldDate = initSchedule.getDate();
        oldTitle = initSchedule.getTitle();
        oldTopic = initSchedule.getTopic();
        oldLink = initSchedule.getLink();
        oldType = initSchedule.getType();
        
        newDate = initDate;
        newTitle = initTitle;
        newTopic = initTopic;
        newLink = initLink;
        newType = initType;
    }
    @Override
    public void doTransaction() {
        schedule.setDate(newDate);
        schedule.setTitle(newTitle);
        schedule.setTopic(newTopic);
        schedule.setLink(newLink);
        schedule.setType(newType);
    }

    @Override
    public void undoTransaction() {
        schedule.setDate(oldDate);
        schedule.setTitle(oldTitle);
        schedule.setTopic(oldTopic);
        schedule.setLink(oldLink);
        schedule.setType(oldType);
    }
    
}
