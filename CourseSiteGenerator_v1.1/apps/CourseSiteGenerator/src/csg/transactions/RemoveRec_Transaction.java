/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.MTData;
import csg.data.SubLecture;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class RemoveRec_Transaction implements jTPS_Transaction{
    MTData data;
    SubLecture rec;
    
    public RemoveRec_Transaction(MTData initData, SubLecture initRec){
        data = initData;
        rec = initRec;
    }
    
    @Override
    public void doTransaction() {
        data.removeRecitation(rec);
    }

    @Override
    public void undoTransaction() {
        data.addRecitation(rec);
    }
}
