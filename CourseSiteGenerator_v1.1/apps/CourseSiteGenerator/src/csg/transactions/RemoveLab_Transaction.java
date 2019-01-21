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
public class RemoveLab_Transaction implements jTPS_Transaction{
    MTData data;
    SubLecture lab;
    
    public RemoveLab_Transaction(MTData initData, SubLecture initLab){
        data = initData;
        lab = initLab;
    }
    
    @Override
    public void doTransaction() {
        data.removeLab(lab);
    }

    @Override
    public void undoTransaction() {
        data.addLab(lab);
    }
}
