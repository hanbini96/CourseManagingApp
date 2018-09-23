package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;

/**
 *
 * @author HanBin, Han as in HanSolo Bin as in Trash Bin
 */
public class ClickOH_Transaction implements jTPS_Transaction {
    OfficeHoursData data;
    TeachingAssistantPrototype ta;
    TimeSlot slot;
    int col;
    
    public ClickOH_Transaction(OfficeHoursData initData, TeachingAssistantPrototype initTA, TimeSlot initSlot, int initCol) {
        data = initData;
        ta = initTA;
        slot = initSlot;
        col = initCol;
    }

    @Override
    public void doTransaction() {
        if (!slot.addTA(data.getColumnDayOfWeek(col), ta)){
                slot.removeTA(data.getColumnDayOfWeek(col), ta);
        }
    }

    @Override
    public void undoTransaction() {
        if (!slot.addTA(data.getColumnDayOfWeek(col), ta)){
                slot.removeTA(data.getColumnDayOfWeek(col), ta);
        }
    }
}
