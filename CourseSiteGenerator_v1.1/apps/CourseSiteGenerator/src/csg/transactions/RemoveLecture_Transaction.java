/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.Lecture;
import csg.data.MTData;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class RemoveLecture_Transaction implements jTPS_Transaction{
    MTData data;
    Lecture lecture;
    
    public RemoveLecture_Transaction(MTData initData, Lecture initLecture){
        data = initData;
        lecture = initLecture;
    }
    
    @Override
    public void doTransaction() {
        data.removeLecture(lecture);
    }

    @Override
    public void undoTransaction() {
        data.addLecture(lecture);
    }
}
