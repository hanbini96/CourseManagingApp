/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.control.CheckBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class CheckBox_Transaction implements jTPS_Transaction{
    CheckBox box;
    
    public CheckBox_Transaction(CheckBox initBox){
        box = initBox;
        box.setSelected(!box.isSelected());
    }
    
    @Override
    public void doTransaction() {
        box.setSelected(!box.isSelected());
    }

    @Override
    public void undoTransaction() {
        box.setSelected(!box.isSelected());
    }
    
}
