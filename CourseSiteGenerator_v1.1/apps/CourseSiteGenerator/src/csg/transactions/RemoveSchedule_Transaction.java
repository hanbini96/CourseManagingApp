/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.ScheduleData;
import csg.data.SchedulePrototype;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class RemoveSchedule_Transaction implements jTPS_Transaction{
    ScheduleData data;
    SchedulePrototype schedule;
    
    public RemoveSchedule_Transaction(ScheduleData initData, SchedulePrototype initSchedule){
        data = initData;
        schedule = initSchedule;
    }
    
    @Override
    public void doTransaction() {
        data.removeSchedule(schedule);
    }

    @Override
    public void undoTransaction() {
        data.addSchedule(schedule);
    }
    
}
