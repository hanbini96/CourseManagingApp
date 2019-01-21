/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.controllers;

import csg.CSGApp;
import static csg.CSGPropertyType.MT_LAB_TABLE_VIEW;
import static csg.CSGPropertyType.MT_LECTURE_TABLE_VIEW;
import static csg.CSGPropertyType.MT_REC_TABLE_VIEW;
import csg.data.CSGData;
import csg.data.Lecture;
import csg.data.MTData;
import csg.data.SubLecture;
import csg.transactions.AddLab_Transaction;
import csg.transactions.AddLecture_Transaction;
import csg.transactions.AddRec_Transaction;
import csg.transactions.RemoveLab_Transaction;
import csg.transactions.RemoveLecture_Transaction;
import csg.transactions.RemoveRec_Transaction;
import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;

/**
 *
 * @author hanbin
 */
public class MTController {
    CSGApp app;

    public MTController(CSGApp initApp) {
        app = initApp;
    }
    
    public void processAddLecture(){
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        Lecture newLecture = new Lecture();
        AddLecture_Transaction transaction = new AddLecture_Transaction(data, newLecture);
        app.processTransaction(transaction);
    }
    
    public void processAddRec(){
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        SubLecture newRec = new SubLecture();
        AddRec_Transaction transaction = new AddRec_Transaction(data, newRec);
        app.processTransaction(transaction);
    }
    
    public void processAddLab(){
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        SubLecture newLab = new SubLecture();
        AddLab_Transaction transaction = new AddLab_Transaction(data, newLab);
        app.processTransaction(transaction);
    }

    public void processRemoveLecture() {
        AppGUIModule gui = app.getGUIModule();
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        TableView<Lecture> tblLecture = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        Lecture lecture = tblLecture.getSelectionModel().getSelectedItem();
        RemoveLecture_Transaction transaction = new RemoveLecture_Transaction(data, lecture);
        app.processTransaction(transaction);
    }

    public void processRemoveRec() {
        AppGUIModule gui = app.getGUIModule();
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        TableView<SubLecture> tblRec = (TableView)gui.getGUINode(MT_REC_TABLE_VIEW);
        SubLecture rec = tblRec.getSelectionModel().getSelectedItem();
        RemoveRec_Transaction transaction = new RemoveRec_Transaction(data, rec);
        app.processTransaction(transaction);
    }

    public void processRemoveLab() {
        AppGUIModule gui = app.getGUIModule();
        MTData data = ((CSGData)app.getDataComponent()).getMTData();
        TableView<SubLecture> tblLab = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        SubLecture lab = tblLab.getSelectionModel().getSelectedItem();
        RemoveLab_Transaction transaction = new RemoveLab_Transaction(data, lab);
        app.processTransaction(transaction);
    }
}
