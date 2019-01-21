package csg.transactions;

import jtps.jTPS_Transaction;
import csg.data.CSGData;
import csg.data.TeachingAssistantPrototype;
import javafx.scene.control.ComboBox;

/**
 *
 * @author McKillaGorilla
 */
public class AddComboItem_Transaction implements jTPS_Transaction {
    ComboBox comboBox;
    Object newValue;

    public AddComboItem_Transaction(ComboBox<String> comboBox, Object newValue) {
        this.comboBox = comboBox;
        this.newValue =  newValue;
    }

    @Override
    public void doTransaction() {
        comboBox.getItems().add(newValue);
    }

    @Override
    public void undoTransaction() {
        comboBox.getItems().remove(newValue);
    }
}
