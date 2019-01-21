/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace.controllers;

import csg.CSGApp;
import csg.transactions.ChangeTextArea_Transaction;
import javafx.scene.control.TextArea;

/**
 *
 * @author hanbin
 */
public class SyllabusController {
    CSGApp app;

    public SyllabusController(CSGApp initApp) {
        app = initApp;
    }

    public void processChangeTextArea(TextArea textArea, String oldValue, String newValue) {
        ChangeTextArea_Transaction transactoin = new ChangeTextArea_Transaction(textArea, oldValue, newValue);
        app.processTransaction(transactoin);
        
    }
}
