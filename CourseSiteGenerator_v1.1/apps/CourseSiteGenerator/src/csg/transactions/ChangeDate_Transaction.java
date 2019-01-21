/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class ChangeDate_Transaction  implements jTPS_Transaction {
    DatePicker datePicker;
    LocalDate oldValue;
    LocalDate newValue;
    
    public ChangeDate_Transaction(DatePicker datePicker, LocalDate oldValue, LocalDate newValue){
        this.datePicker = datePicker;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    @Override
    public void doTransaction() {
        datePicker.setValue(newValue);
    }

    @Override
    public void undoTransaction() {
        datePicker.setValue(oldValue);
    }
    
}
