package csg.transactions;

import jtps.jTPS_Transaction;
import csg.CSGApp;
import csg.data.CSGData;
import csg.data.OfficeHoursData;
import csg.data.TeachingAssistantPrototype;

public class PasteTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype taToPaste;

    public PasteTA_Transaction(  CSGApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        OfficeHoursData data = ((CSGData)app.getDataComponent()).getOHData();
        data.removeTA(taToPaste);
    }   
}