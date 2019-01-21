package csg.transactions;

import jtps.jTPS_Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import csg.CSGApp;
import static csg.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.OfficeHoursData;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import javafx.scene.control.TableView;

public class RemoveTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype taToCut;
    HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours;

    public RemoveTA_Transaction(CSGApp initApp, 
            TeachingAssistantPrototype initTAToCut, 
            HashMap<TimeSlot, ArrayList<DayOfWeek>> initOfficeHours) {
        app = initApp;
        taToCut = initTAToCut;
        officeHours = initOfficeHours;
    }

    @Override
    public void doTransaction() {
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        data.removeTA(taToCut, officeHours);
        ((TableView)app.getGUIModule().getGUINode(OH_OFFICE_HOURS_TABLE_VIEW)).refresh();
    }

    @Override
    public void undoTransaction() {
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        data.addTA(taToCut, officeHours);
        ((TableView)app.getGUIModule().getGUINode(OH_OFFICE_HOURS_TABLE_VIEW)).refresh();
    }   
}