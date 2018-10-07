package oh.transactions;

import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import static oh.OfficeHoursPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;

/**
 *
 * @author HanBin, Han as in HanSolo Bin as in Trash Bin
 */
public class ClickOH_Transaction implements jTPS_Transaction {
    TableView officeHoursTableView;
    OfficeHoursData data;
    TeachingAssistantPrototype ta;
    TimeSlot slot;
    int col;
    
    public ClickOH_Transaction(TableView ohTableView, OfficeHoursData initData, TeachingAssistantPrototype initTA, TimeSlot initSlot, int initCol) {
        officeHoursTableView = ohTableView;
        data = initData;
        ta = initTA;
        slot = initSlot;
        col = initCol;
    }

    @Override
    public void doTransaction() {
        if (!slot.dowContainsTa(data.getColumnDayOfWeek(col), ta)){
            slot.addTA(data.getColumnDayOfWeek(col), ta);
        }
        else{
            slot.removeTA(data.getColumnDayOfWeek(col), ta);
        }
        
        officeHoursTableView.refresh();
    }

    @Override
    public void undoTransaction() {
        if (!slot.dowContainsTa(data.getColumnDayOfWeek(col), ta)){
            slot.addTA(data.getColumnDayOfWeek(col), ta);
        }
        else{
            slot.removeTA(data.getColumnDayOfWeek(col), ta);
        }
        officeHoursTableView.refresh();
    }
}
