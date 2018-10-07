package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;

/**
 *
 * @author HanBin, Han as in HanSolo Bin as in Trash Bin
 */
public class ClickOH_Transaction implements jTPS_Transaction {
    OfficeHoursData data;
    TeachingAssistantPrototype ta;
    TimeSlot slot;
    DayOfWeek dow;
    
    public ClickOH_Transaction(OfficeHoursData initData, TeachingAssistantPrototype initTA, TimeSlot initSlot, DayOfWeek initDow) {
        data = initData;
        ta = initTA;
        slot = initSlot;
        dow = initDow;
    }

    @Override
    public void doTransaction() {
        slot.toogleTA(dow, ta);
    }

    @Override
    public void undoTransaction() {
        slot.toogleTA(dow, ta);
    }
}
