/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class ChangeCombo_Transaction implements jTPS_Transaction{
    ComboBox box;
    String oldValue;
    String newValue;
    
    public ChangeCombo_Transaction(ComboBox box, String oldValue, String newValue){
        this.box = box;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        box.getSelectionModel().select(newValue);
    }

    @Override
    public void undoTransaction() {
        box.getSelectionModel().select(oldValue);
    }
    
}
