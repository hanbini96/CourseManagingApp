/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.SiteData;
import csg.data.SiteData.LogoType;
import jtps.jTPS_Transaction;

/**
 *
 * @author hanbin
 */
public class ImageUpdate_Transaction implements jTPS_Transaction {
    SiteData data;
    String oldSrc;
    String oldHref;
    
    String newSrc;
    String newHref;
    LogoType type;
    
    public ImageUpdate_Transaction(SiteData initData, String initSrc, String initHref, LogoType initType){
        data = initData;
        type = initType;
        oldSrc = data.getLogoInfo(initType);
        oldHref = data.getHref(initType);
        
        newSrc = initSrc;
        newHref = initHref;
    }
    
    @Override
    public void doTransaction() {
        data.setHref(newHref, type);
        data.setLogoInfo(newSrc, type);
        data.updateLogos();
    }

    @Override
    public void undoTransaction() {
        data.setHref(oldHref, type);
        data.setLogoInfo(oldSrc, type);
        data.updateLogos();
    }
    
}
