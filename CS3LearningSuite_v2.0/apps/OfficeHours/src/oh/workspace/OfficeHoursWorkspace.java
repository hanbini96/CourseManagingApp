package oh.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oh.OfficeHoursApp;
import oh.OfficeHoursPropertyType;
import static oh.OfficeHoursPropertyType.*;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.workspace.controllers.OfficeHoursController;
import oh.workspace.foolproof.OfficeHoursFoolproofDesign;
import static oh.workspace.style.OHStyle.*;

/**
 *
 * @author McKillaGorilla
 */
public class OfficeHoursWorkspace extends AppWorkspaceComponent {

    public OfficeHoursWorkspace(OfficeHoursApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // SETUP FOOLPROOF DESIGN FOR THIS APP
        initFoolproofDesign();
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        // PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder ohBuilder = app.getGUIModule().getNodesBuilder();

        // INIT THE HEADER ON THE LEFT
        VBox leftPane = ohBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = ohBuilder.buildHBox(OH_TAS_HEADER_PANE, leftPane, CLASS_OH_BOX, ENABLED);
        Label leftHead = ohBuilder.buildLabel(OfficeHoursPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = ohBuilder.buildTableView(OH_TAS_TABLE_VIEW, leftPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = ohBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));

        TableColumn emailColumn = ohBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0/4.0));
        
        TableColumn slotColumn = ohBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_COLUMN_TIMESLOT);
        slotColumn.setCellValueFactory(new PropertyValueFactory<String, Integer>("timeslot"));
        slotColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0/4.0));
        
        // ADD BOX FOR ADDING A TA
        HBox taBox = ohBuilder.buildHBox(OH_ADD_TA_PANE, leftPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, OH_NAME_BUTTON_TEXT, ENABLED);
        ohBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, OH_EMAIL_BUTTON_TEXT, ENABLED);
        ohBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, ENABLED);
        
        /*
        TextField name = (TextField)app.getGUIModule().getGUINode(OH_NAME_TEXT_FIELD);
        name.setPromptText(props.getProperty(OH_NAME_BUTTON_TEXT));
        TextField email = (TextField)app.getGUIModule().getGUINode(OH_EMAIL_TEXT_FIELD);
        email.setPromptText(props.getProperty(OH_EMAIL_BUTTON_TEXT));
        */
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = ohBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = ohBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        ohBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);

        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = ohBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        TableColumn startTimeColumn = ohBuilder.buildTableColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn endTimeColumn = ohBuilder.buildTableColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_TIME_COLUMN);
        TableColumn mondayColumn = ohBuilder.buildTableColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn tuesdayColumn = ohBuilder.buildTableColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn wednesdayColumn = ohBuilder.buildTableColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn thursdayColumn = ohBuilder.buildTableColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        TableColumn fridayColumn = ohBuilder.buildTableColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_DAY_OF_WEEK_COLUMN);
        
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("endTime"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<String, String>("friday"));
        for (int i = 0; i < officeHoursTable.getColumns().size(); i++) {
            ((TableColumn)officeHoursTable.getColumns().get(i)).prefWidthProperty().bind(officeHoursTable.widthProperty().multiply(1.0/7.0));
        }
        
        // enable cell selection
        officeHoursTable.getSelectionModel().setCellSelectionEnabled(ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setDividerPositions(.4);
        workspace = new BorderPane();

        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane)workspace).setCenter(sPane);
    }

    private void initControllers() {
        OfficeHoursController controller = new OfficeHoursController((OfficeHoursApp) app);
        AppGUIModule gui = app.getGUIModule();
        
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
        
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        
        officeHoursTableView.setOnMouseClicked(e -> {
            if (taTableView.getSelectionModel().getSelectedItem() != null) {
                controller.processClickOH(taTableView.getSelectionModel().getSelectedItem());
            }
        });
        
        // DON'T LET ANYONE SORT THE TABLES
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn)officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
    }

    private void initFoolproofDesign() {
        // AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new OfficeHoursFoolproofDesign((OfficeHoursApp) app));
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
}
