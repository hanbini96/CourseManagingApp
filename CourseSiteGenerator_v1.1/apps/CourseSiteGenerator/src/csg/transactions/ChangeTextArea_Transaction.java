/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class ChangeTextArea_Transaction implements jTPS_Transaction{
    TextArea text;
    String oldValue;
    String newValue;
    
    public ChangeTextArea_Transaction(TextArea text, String oldValue, String newValue){
        this.text = text;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        text.setText(newValue);
    }

    @Override
    public void undoTransaction() {
        text.setText(oldValue);
    }
}
