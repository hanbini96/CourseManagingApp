/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import djf.ui.AppNodesBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import csg.CSGApp;
import static csg.CSGPropertyType.*;
import csg.data.Lecture;
import csg.data.SubLecture;
import csg.data.TeachingAssistantPrototype;
import csg.data.SchedulePrototype;
import csg.data.SiteData.LogoType;
import csg.data.TimeSlot;
import csg.workspace.controllers.MTController;
import csg.workspace.controllers.OfficeHoursController;
import csg.workspace.controllers.ScheduleController;
import csg.workspace.controllers.SiteController;
import csg.workspace.controllers.SyllabusController;
import csg.workspace.dialogs.FileSelecter;
import csg.workspace.dialogs.TADialog;
import csg.workspace.foolproof.OfficeHoursFoolproofDesign;
import csg.workspace.foolproof.ScheduleFoolproofDesign;
import csg.workspace.foolproof.SiteFoolproofDesign;
import static csg.workspace.style.CSGStyle.*;
import static djf.AppTemplate.PATH_ICONS;
import static djf.modules.AppGUIModule.DISABLED;
import static djf.modules.AppGUIModule.ENABLED;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

/**
 *
 * @author hanbin
 */
public class CSGWorkspace  extends AppWorkspaceComponent {

    public CSGWorkspace(CSGApp app) {
        super(app);
        
        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // INIT THE FOOLPROOF DESIGN
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CSGApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder csgBuilder = app.getGUIModule().getNodesBuilder();
        
        TabPane tabPane = csgBuilder.buildTabPane(TAB_PANE, null, CLASS_CSG_PANE, ENABLED);
        Tab siteTab = csgBuilder.buildTab(SITE_TAB, tabPane, CLASS_CSG_TAB, ENABLED);
        Tab syllabusTab = csgBuilder.buildTab(SYLLABUS_TAB, tabPane, CLASS_CSG_TAB, ENABLED);
        Tab mtTab = csgBuilder.buildTab(MT_TAB, tabPane, CLASS_CSG_TAB, ENABLED);
        Tab ohTab = csgBuilder.buildTab(OH_TAB, tabPane, CLASS_CSG_TAB, ENABLED);
        Tab scheduleTab = csgBuilder.buildTab(SCHEDULE_TAB, tabPane, CLASS_CSG_TAB, ENABLED);
        tabPane.tabMinWidthProperty().bind((tabPane.widthProperty().subtract(110).divide(5)));
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        setupSiteWorkspace(siteTab, csgBuilder);
        setupSyllabusWorkspace(syllabusTab, csgBuilder);
        setupMTWorkspace(mtTab, csgBuilder);
        setupOHWorkspace(ohTab, csgBuilder);
        setupScheduleWorkspace(scheduleTab, csgBuilder);
        
        workspace = new BorderPane();

        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(tabPane);
    }
    
    /*
    User selects appropriate Subject, Class Number, Semester, and Year from drop
        down boxes or Types values in if not available (typed in values should then be
        saved as settings so as to appear as options for future sites).
    User clicks on Pages checkboxes to select applicable export pages
    User clicks on Style buttons to select site images
    User selects appropriate CSS style file from combo box
    */
    private void setupSiteWorkspace(Tab siteTab, AppNodesBuilder csgBuilder){
        VBox siteBox = csgBuilder.buildVBox(SITE_BOX, null, CLASS_CSG_VBOX, ENABLED);
        // Create Banner Pane
        GridPane bannerPane = csgBuilder.buildGridPane(SITE_BANNER_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SITE_BANNER_LABEL, bannerPane, 0, 0, 3, 1, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildLabel(SITE_SUBJECT_LABEL, bannerPane, 0, 1, 2, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildComboBox(SITE_SUBJECT_COMBOBOX, bannerPane, 1, 1, 2, 1, CLASS_CSG_COMBO_BOX, ENABLED, ENABLED);
        csgBuilder.buildLabel(SITE_NUMBER_LABEL, bannerPane, 2, 1, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildComboBox(SITE_NUMBER_COMBOBOX, bannerPane, 3, 1, 1, 1, CLASS_CSG_COMBO_BOX, ENABLED, ENABLED);
        csgBuilder.buildLabel(SITE_SEMESTER_LABEL, bannerPane, 0, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildComboBox(SITE_SEMESTER_COMBOBOX, bannerPane, 1, 2, 1, 1, CLASS_CSG_COMBO_BOX, ENABLED, ENABLED);
        csgBuilder.buildLabel(SITE_YEAR_LABEL, bannerPane, 2, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildComboBox(SITE_YEAR_COMBOBOX, bannerPane, 3, 2, 1, 1, CLASS_CSG_COMBO_BOX, ENABLED, ENABLED);
        csgBuilder.buildLabel(SITE_TITLE_LABEL, bannerPane, 0, 3, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SITE_TITLE_TEXT_FIELD, bannerPane, 1, 3, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildLabel(SITE_EXPORTDIR_LABEL, bannerPane, 0, 4, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SITE_EXPORTDIR, bannerPane, 1, 4, 1, 1, CLASS_CSG_LABEL, ENABLED);
        
        // Create Pages Box
        GridPane pagesPane = csgBuilder.buildGridPane(SITE_PAGES_BOX, siteBox, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SITE_PAGES_LABEL, pagesPane, 0, 1, 1, 1, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildCheckBox(SITE_HOME_CHECKBOX, pagesPane, 1, 1, 1, 1, CLASS_CSG_CHECK_BOX, ENABLED);
        csgBuilder.buildCheckBox(SITE_SYLLABUS_CHECKBOX, pagesPane, 2, 1, 1, 1, CLASS_CSG_CHECK_BOX, ENABLED);
        csgBuilder.buildCheckBox(SITE_SCHEDULE_CHECKBOX, pagesPane, 3, 1, 1, 1, CLASS_CSG_CHECK_BOX, ENABLED);
        csgBuilder.buildCheckBox(SITE_HWS_CHECKBOX, pagesPane, 4, 1, 1, 1, CLASS_CSG_CHECK_BOX, ENABLED);
        
        // Create Style Pane
        GridPane stylePane = csgBuilder.buildGridPane(SITE_STYLE_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SITE_STYLE_LABEL, stylePane, 0, 0, 3, 1, CLASS_CSG_HEADER_LABEL, ENABLED);
        Button favButton = csgBuilder.buildTextButton(SITE_FAV_ICON_BUTTON, stylePane, 0, 1, 1, 1, CLASS_CSG_BUTTON, ENABLED);
        ImageView favIcon = csgBuilder.buildIcon(SITE_FAV_ICON, stylePane, 2, 1, 1, 1, CLASS_CSG_ICON, ENABLED);
        Button navButton = csgBuilder.buildTextButton(SITE_NAV_BAR_IMAGE_BUTTON, stylePane, 0, 2, 1, 1, CLASS_CSG_BUTTON, ENABLED);
        ImageView navIcon = csgBuilder.buildIcon(SITE_NAV_BAR_ICON, stylePane, 2, 2, 1, 1, CLASS_CSG_ICON, ENABLED);
        Button leftFooterButton = csgBuilder.buildTextButton(SITE_LEFT_FOOTER_IMAGE_BUTTON, stylePane, 0, 3, 1, 1, CLASS_CSG_BUTTON, ENABLED);
        ImageView leftFootIcon = csgBuilder.buildIcon(SITE_LEFT_FOOT_ICON, stylePane, 2, 3, 1, 1, CLASS_CSG_ICON, ENABLED);
        Button rightFooterButton = csgBuilder.buildTextButton(SITE_RIGHT_FOOTER_IMAGE_BUTTON, stylePane, 0, 4, 1, 1, CLASS_CSG_BUTTON, ENABLED);
        ImageView rightFootIcon = csgBuilder.buildIcon(SITE_RIGHT_FOOT_ICON, stylePane, 2, 4, 1, 1, CLASS_CSG_ICON, ENABLED);
        csgBuilder.buildLabel(SITE_STYLE_SHEET_LABEL, stylePane, 0, 5, 1, 1, CLASS_CSG_LABEL, ENABLED);
        ComboBox styleList = csgBuilder.buildComboBox(SITE_STYLE_SHEET_COMBOBOX, stylePane, 2, 5, 1, 1, CLASS_CSG_COMBO_BOX, DISABLED, ENABLED);
        csgBuilder.buildLabel(SITE_STYLE_NOTE, stylePane, 0, 6, 3, 1, CLASS_CSG_LABEL, ENABLED);
        
        favButton.setMaxWidth(Double.MAX_VALUE);
        navButton.setMaxWidth(Double.MAX_VALUE);
        leftFooterButton.setMaxWidth(Double.MAX_VALUE);
        rightFooterButton.setMaxWidth(Double.MAX_VALUE);
        
        styleList.getItems().setAll(FileSelecter.getStyleSheets());
        
        // Create Instructor Pane
        GridPane instructorPane = csgBuilder.buildGridPane(SITE_INSTRUCTOR_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SITE_INSTRUCTOR_LABEL, instructorPane, 0, 0, 3, 1, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildLabel(SITE_NAME_LABEL, instructorPane, 0, 1, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SITE_NAME_TEXT_FIELD, instructorPane, 1, 1, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildLabel(SITE_ROOM_LABEL, instructorPane, 2, 1, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SITE_ROOM_TEXT_FIELD, instructorPane, 3, 1, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildLabel(SITE_EMAIL_LABEL, instructorPane, 0, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SITE_EMAIL_TEXT_FIELD, instructorPane, 1, 2, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildLabel(SITE_HOMEPAGE_LABEL, instructorPane, 2, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SITE_HOMEPAGE_TEXT_FIELD, instructorPane, 3, 2, 1, 1, CLASS_CSG_TEXT_FIELD, ENABLED);
        TextArea ohTextArea = csgBuilder.buildTextArea(SITE_OFFICE_HOURS_TEXT_AREA, instructorPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(ohTextArea,SITE_OFFICE_HOURS_LABEL,SITE_OPEN_BUTTON, instructorPane,0, 3, 1, 1, csgBuilder);
        ohTextArea.prefWidthProperty().bind(instructorPane.widthProperty());
        //ohTextArea.prefHeightProperty().bind(instructorPane.heightProperty());
        
        instructorPane.maxWidthProperty().bind(siteBox.prefWidthProperty());
        ScrollPane sitePane = new ScrollPane();
        sitePane.setContent(siteBox);
        sitePane.setFitToHeight(true);
        sitePane.setFitToWidth(true);
        siteTab.setContent(sitePane);
    }
    
    private void setupSyllabusWorkspace(Tab syllabusTab, AppNodesBuilder csgBuilder){
        VBox siteBox = csgBuilder.buildVBox(SYLL_PANE, null, CLASS_CSG_VBOX, ENABLED);
        GridPane descriptionPane = csgBuilder.buildGridPane(SYLL_DESC_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea descTextArea = csgBuilder.buildTextArea(SYLL_DESC_TEXT_AREA, descriptionPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(descTextArea, SYLL_DESC_LABEL, SYLL_DESC_BUTTON, descriptionPane, 0, 0, 1, 1, csgBuilder);
        descTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane topicsPane = csgBuilder.buildGridPane(SYLL_TOPICS_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea topicsTextArea = csgBuilder.buildTextArea(SYLL_TOPICS_TEXT_AREA, topicsPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(topicsTextArea, SYLL_TOPICS_LABEL, SYLL_TOPICS_BUTTON, topicsPane, 0, 0, 1, 1, csgBuilder);        
        topicsTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());

        GridPane prereqPane = csgBuilder.buildGridPane(SYLL_PREREQ_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea prereqTextArea = csgBuilder.buildTextArea(SYLL_PREREQ_TEXT_AREA, prereqPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(prereqTextArea, SYLL_PREREQ_LABEL, SYLL_PREREQ_BUTTON, prereqPane, 0, 0, 1, 1, csgBuilder);
        prereqTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane outcomesPane = csgBuilder.buildGridPane(SYLL_OUTCOMES_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea outcomesTextArea = csgBuilder.buildTextArea(SYLL_OUTCOMES_TEXT_AREA, outcomesPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(outcomesTextArea, SYLL_OUTCOMES_LABEL, SYLL_OUTCOMES_BUTTON,outcomesPane, 0, 0, 1, 1, csgBuilder);
        outcomesTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane textbooksPane = csgBuilder.buildGridPane(SYLL_TEXTBOOKS_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea textbooksTextArea = csgBuilder.buildTextArea(SYLL_TEXTBOOKS_TEXT_AREA, textbooksPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(textbooksTextArea, SYLL_TEXTBOOKS_LABEL, SYLL_TEXTBOOKS_BUTTON, textbooksPane, 0, 0, 1, 1, csgBuilder);
        textbooksTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane gradedComponentsPane = csgBuilder.buildGridPane(SYLL_GRADE_COMP_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea gradeCompTextArea = csgBuilder.buildTextArea(SYLL_GRADE_COMP_TEXT_AREA, gradedComponentsPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(gradeCompTextArea, SYLL_GRADE_COMP_LABEL, SYLL_GRADE_COMP_BUTTON, gradedComponentsPane, 0, 0, 1, 1, csgBuilder);
        gradeCompTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane gradingNotePane = csgBuilder.buildGridPane(SYLL_GRADE_NOTE_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea gradeNoteTextArea = csgBuilder.buildTextArea(SYLL_GRADE_NOTE_TEXT_AREA, gradingNotePane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(gradeNoteTextArea, SYLL_GRADE_NOTE_LABEL, SYLL_GRADE_NOTE_BUTTON, gradingNotePane, 0, 0, 1, 1, csgBuilder);
        gradeNoteTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane academicDishonestyPane = csgBuilder.buildGridPane(SYLL_AD_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea adTextArea = csgBuilder.buildTextArea(SYLL_AD_TEXT_AREA, academicDishonestyPane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(adTextArea, SYLL_AD_LABEL, SYLL_AD_BUTTON, academicDishonestyPane, 0, 0, 1, 1, csgBuilder);
        adTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        GridPane specialAssistancePane = csgBuilder.buildGridPane(SYLL_SA_PANE, siteBox, CLASS_CSG_PANE, ENABLED);
        TextArea saTextArea = csgBuilder.buildTextArea(SYLL_SA_TEXT_AREA, specialAssistancePane, CLASS_CSG_TEXT_FIELD, ENABLED);
        setupOpenBox(saTextArea, SYLL_SA_LABEL, SYLL_SA_BUTTON, specialAssistancePane, 0, 0, 1, 1, csgBuilder);
        saTextArea.prefWidthProperty().bind(descriptionPane.widthProperty());
        
        
        ScrollPane syllabusPane = new ScrollPane();
        syllabusPane.setContent(siteBox);
        syllabusPane.setFitToHeight(true);
        syllabusPane.setFitToWidth(true);
        syllabusTab.setContent(syllabusPane);
    }
    
    
    
    private void setupOpenBox(Node node, Object labelID, Object buttonID, GridPane parentPane, 
            int col, int row, int colSpan, int rowSpan, 
            AppNodesBuilder csgBuilder){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ToggleButton openTextButton = csgBuilder.buildIconToggleButton(buttonID, parentPane, col, row, colSpan, rowSpan, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildLabel(labelID, parentPane, col+1, row, colSpan, rowSpan, CLASS_CSG_LABEL, ENABLED);
        
        openTextButton.selectedProperty().addListener(e -> {
            if (openTextButton.isSelected()){
                String iconProperty = "SITE_CLOSE_BUTTON_ICON";
                String imagePath = FILE_PROTOCOL + PATH_ICONS + props.getProperty(iconProperty);
                Image buttonImage = new Image(imagePath);
                if (!buttonImage.isError()) {
                    openTextButton.setGraphic(new ImageView(buttonImage));
                }
                //textField.setVisible(ENABLED);
                if (!parentPane.getChildren().contains(node)){
                    parentPane.add(node, 0, row+1, 5, 1);
                }
            }
            else {
                String iconProperty = "SITE_OPEN_BUTTON_ICON";
                String imagePath = FILE_PROTOCOL + PATH_ICONS + props.getProperty(iconProperty);
                Image buttonImage = new Image(imagePath);
                if (!buttonImage.isError()) {
                    openTextButton.setGraphic(new ImageView(buttonImage));
                }
                //textField.setVisible(DISABLED);
                parentPane.getChildren().remove(node);
            }
        });
        // reset the button state to apply the changes
        openTextButton.selectedProperty().set(ENABLED);
        openTextButton.selectedProperty().set(DISABLED);
    }
    
    private void setupMTWorkspace(Tab mtTab, AppNodesBuilder csgBuilder){
        VBox mtPane = csgBuilder.buildVBox(MT_PANE, null, CLASS_CSG_VBOX, ENABLED);
        VBox lecturePane = csgBuilder.buildVBox(MT_LECTURE_PANE, mtPane, CLASS_CSG_PANE, ENABLED);
        HBox lectureHeader = csgBuilder.buildHBox(MT_LECTURE_HEADER, lecturePane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildIconButton(MT_ADD_LECTURE_BUTTON, lectureHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildIconButton(MT_REMOVE_LECTURE_BUTTON, lectureHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildLabel(MT_LECTURE_LABEL, lectureHeader, CLASS_CSG_LABEL, ENABLED);
        
        TableView<Lecture> lectureTable = csgBuilder.buildTableView(MT_LECTURE_TABLE_VIEW, lecturePane, CLASS_CSG_TABLE_VIEW, ENABLED);
        TableColumn lectureSectionColumn = csgBuilder.buildTableColumn(MT_LECTURE_SECTION_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        TableColumn lectureDayColumn = csgBuilder.buildTableColumn(MT_LECTURE_DAY_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        TableColumn lectureTimeColumn = csgBuilder.buildTableColumn(MT_LECTURE_TIME_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        TableColumn lectureRoomColumn = csgBuilder.buildTableColumn(MT_LECTURE_ROOM_TABLE_COLUMN, lectureTable, CLASS_CSG_COLUMN);
        lectureSectionColumn.setCellValueFactory(new PropertyValueFactory<Lecture, String>("section"));
        lectureSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureSectionColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(CellEditEvent<Lecture, String> t) {
                    ((Lecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setSection(t.getNewValue());
                }
            }
        );
        lectureDayColumn.setCellValueFactory(new PropertyValueFactory<Lecture, String>("day"));
        lectureDayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureDayColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(CellEditEvent<Lecture, String> t) {
                    ((Lecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setDay(t.getNewValue());
                }
            }
        );
        lectureTimeColumn.setCellValueFactory(new PropertyValueFactory<Lecture, String>("time"));
        lectureTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureTimeColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(CellEditEvent<Lecture, String> t) {
                    ((Lecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setTime(t.getNewValue());
                }
            }
        );
        lectureRoomColumn.setCellValueFactory(new PropertyValueFactory<Lecture, String>("room"));
        lectureRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lectureRoomColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<Lecture, String>>() {
                @Override
                public void handle(CellEditEvent<Lecture, String> t) {
                    ((Lecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setRoom(t.getNewValue());
                }
            }
        );
        lectureSectionColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(3.0 / 12.0));
        lectureDayColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0 / 12.0));
        lectureTimeColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(4.0 / 12.0));
        lectureRoomColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(3.0 / 12.0));
        lectureTable.setEditable(ENABLED);
        
        VBox recPane = csgBuilder.buildVBox(MT_REC_PANE, mtPane, CLASS_CSG_PANE, ENABLED);
        HBox recHeader = csgBuilder.buildHBox(MT_REC_HEADER, recPane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildIconButton(MT_ADD_REC_BUTTON, recHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildIconButton(MT_REMOVE_REC_BUTTON, recHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildLabel(MT_REC_LABEL, recHeader, CLASS_CSG_LABEL, ENABLED);
        
        TableView<SubLecture> recitationTable = csgBuilder.buildTableView(MT_REC_TABLE_VIEW, recPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        TableColumn recSectionColumn = csgBuilder.buildTableColumn(MT_REC_SECTION_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn recDayTimeColumn = csgBuilder.buildTableColumn(MT_REC_DAY_TIME_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn recRoomColumn = csgBuilder.buildTableColumn(MT_REC_ROOM_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn recTa1Column = csgBuilder.buildTableColumn(MT_REC_TA1_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        TableColumn recTa2Column = csgBuilder.buildTableColumn(MT_REC_TA2_TABLE_COLUMN, recitationTable, CLASS_CSG_COLUMN);
        recSectionColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("section"));
        recSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recSectionColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setSection(t.getNewValue());
                }
            }
        );
        recDayTimeColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("daytime"));
        recDayTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recDayTimeColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setDayTime(t.getNewValue());
                }
            }
        );
        recRoomColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("room"));
        recRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recRoomColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setRoom(t.getNewValue());
                }
            }
        );
        recTa1Column.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("ta1"));
        lectureSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recTa1Column.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setTa1(t.getNewValue());
                }
            }
        );
        recTa2Column.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("ta2"));
        recTa2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        recTa2Column.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setTa2(t.getNewValue());
                }
            }
        );
        recSectionColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 17.0));
        recDayTimeColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(4.0 / 17.0));
        recRoomColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(3.0 / 17.0));
        recTa1Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(4.0 / 17.0));
        recTa2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(4.0 / 17.0));
        recitationTable.setEditable(ENABLED);
        
        VBox labPane = csgBuilder.buildVBox(MT_LAB_PANE, mtPane, CLASS_CSG_PANE, ENABLED);
        HBox labHeader = csgBuilder.buildHBox(MT_LAB_HEADER, labPane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildIconButton(MT_ADD_LAB_BUTTON, labHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildIconButton(MT_REMOVE_LAB_BUTTON, labHeader, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildLabel(MT_LAB_LABEL, labHeader, CLASS_CSG_LABEL, ENABLED);
        
        TableView<SubLecture> labTable = csgBuilder.buildTableView(MT_LAB_TABLE_VIEW, labPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        TableColumn labSectionColumn = csgBuilder.buildTableColumn(MT_LAB_SECTION_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn labDayTimeColumn = csgBuilder.buildTableColumn(MT_LAB_DAY_TIME_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn labRoomColumn = csgBuilder.buildTableColumn(MT_LAB_ROOM_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn labTa1Column = csgBuilder.buildTableColumn(MT_LAB_TA1_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        TableColumn labTa2Column = csgBuilder.buildTableColumn(MT_LAB_TA2_TABLE_COLUMN, labTable, CLASS_CSG_COLUMN);
        labSectionColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("section"));
        labSectionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labSectionColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setSection(t.getNewValue());
                }
            }
        );
        labDayTimeColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("daytime"));
        labDayTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labDayTimeColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setDayTime(t.getNewValue());
                }
            }
        );
        labRoomColumn.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("room"));
        labRoomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        labRoomColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setRoom(t.getNewValue());
                }
            }
        );
        labTa1Column.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("ta1"));
        labTa1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        labTa1Column.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setTa1(t.getNewValue());
                }
            }
        );
        labTa2Column.setCellValueFactory(new PropertyValueFactory<SubLecture, String>("ta2"));
        labTa2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        labTa2Column.setOnEditCommit(
            new EventHandler<CellEditEvent<SubLecture, String>>() {
                @Override
                public void handle(CellEditEvent<SubLecture, String> t) {
                    ((SubLecture) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setTa2(t.getNewValue());
                }
            }
        );
        labSectionColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(2.0 / 17.0));
        labDayTimeColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(4.0 / 17.0));
        labRoomColumn.prefWidthProperty().bind(labTable.widthProperty().multiply(3.0 / 17.0));
        labTa1Column.prefWidthProperty().bind(labTable.widthProperty().multiply(4.0 / 17.0));
        labTa2Column.prefWidthProperty().bind(labTable.widthProperty().multiply(4.0 / 17.0));
        labTable.setEditable(ENABLED);
        
        mtTab.setContent(mtPane);
    }
    
    private void setupOHWorkspace(Tab ohTab, AppNodesBuilder csgBuilder){
        // INIT THE HEADER ON THE LEFT
        VBox ohPane = csgBuilder.buildVBox(OH_PANE, null, CLASS_CSG_VBOX, ENABLED);
        HBox tasHeaderBox = csgBuilder.buildHBox(OH_TAS_HEADER_PANE, ohPane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildIconButton(OH_TA_REMOVE_BUTTON, tasHeaderBox, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildLabel(OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_CSG_HEADER_LABEL, ENABLED);
        HBox typeHeaderBox = csgBuilder.buildHBox(OH_GRAD_UNDERGRAD_TAS_PANE, tasHeaderBox, CLASS_CSG_RADIO_BOX, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        csgBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, typeHeaderBox, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, true);
        csgBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, typeHeaderBox, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, false);
        csgBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, typeHeaderBox, CLASS_CSG_RADIO_BUTTON, ENABLED, tg, false);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = csgBuilder.buildTableView(OH_TAS_TABLE_VIEW, ohPane, CLASS_CSG_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = csgBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_CSG_COLUMN);
        TableColumn emailColumn = csgBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_CSG_COLUMN);
        TableColumn slotsColumn = csgBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_CSG_CENTERED_COLUMN);
        TableColumn typeColumn = csgBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_CSG_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = csgBuilder.buildHBox(OH_ADD_TA_PANE, ohPane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_CSG_TEXT_FIELD, ENABLED);
        csgBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_CSG_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        //VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        HBox officeHoursHeaderBox = csgBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, ohPane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildLabel(OH_START_TIME_LABEL, officeHoursHeaderBox, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildComboBox(OH_START_TIME_COMBO_BOX, officeHoursHeaderBox, CLASS_CSG_HEADER_LABEL, DISABLED, ENABLED);
        csgBuilder.buildLabel(OH_END_TIME_LABEL, officeHoursHeaderBox, CLASS_CSG_HEADER_LABEL, ENABLED);
        csgBuilder.buildComboBox(OH_END_TIME_COMBO_BOX, officeHoursHeaderBox, CLASS_CSG_HEADER_LABEL, DISABLED, ENABLED);
        
        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = csgBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, ohPane, CLASS_CSG_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_CSG_DAY_OF_WEEK_COLUMN, "friday");

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        //VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        /*
        SplitPane sPane = new SplitPane(leftPane, rightPane);
        sPane.setDividerPositions(.4);
        */
        ohTab.setContent(ohPane);
    }
    
    private void setupScheduleWorkspace(Tab scheduleTab, AppNodesBuilder csgBuilder){ 
        VBox schedulePane = csgBuilder.buildVBox(SCHEDULE_PANE, null, CLASS_CSG_VBOX, ENABLED);
        GridPane calendarPane = csgBuilder.buildGridPane(SCHEDULE_CALENDAR_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_CALENDAR_LABEL, calendarPane, 0, 0, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_START_MON_LABEL, calendarPane, 0, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildDatePicker(SCHEDULE_START_MON_PICKER, calendarPane,1, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_END_FRI_LABEL, calendarPane, 3, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildDatePicker(SCHEDULE_END_FRI_PICKER, calendarPane,4, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        
        
        GridPane scheduleItemsPane = csgBuilder.buildGridPane(SCHEDULE_ITEMS_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildIconButton(SCHEDULE_REMOVE_BUTTON, scheduleItemsPane, 0, 0, 1, 1, CLASS_CSG_BUTTON, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_ITEMS_LABEL, scheduleItemsPane, 1, 0, 1, 1, CLASS_CSG_LABEL, ENABLED);
        TableView<SchedulePrototype> scheduleItemsTable = csgBuilder.buildTableView(SCHEDULE_ITEMS_TABLE, scheduleItemsPane, 0, 1, 5, 1, CLASS_CSG_TABLE_VIEW, ENABLED);
        TableColumn scheduleTypeColumn = csgBuilder.buildTableColumn(SCHEDULE_TYPE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleDateColumn = csgBuilder.buildTableColumn(SCHEDULE_DATE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleTitleColumn = csgBuilder.buildTableColumn(SCHEDULE_TITLE_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        TableColumn scheduleTopicColumn = csgBuilder.buildTableColumn(SCHEDULE_TOPIC_TABLE_COLUMN, scheduleItemsTable, CLASS_CSG_COLUMN);
        scheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        scheduleDateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        scheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        scheduleTopicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));
        scheduleTypeColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(2.0 / 13.0));
        scheduleDateColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(2.0 / 13.0));
        scheduleTitleColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(3.0 / 13.0));
        scheduleTopicColumn.prefWidthProperty().bind(scheduleItemsTable.widthProperty().multiply(6.0 / 13.0));
        scheduleItemsTable.prefWidthProperty().bind(scheduleItemsPane.widthProperty());
        
        
        GridPane addEditPane = csgBuilder.buildGridPane(SCHEDULE_ADD_EDIT_PANE, schedulePane, CLASS_CSG_PANE, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_ADD_EDIT_LABEL, addEditPane, 0, 0, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_TYPE_LABEL, addEditPane, 0, 1, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildComboBox(SCHEDULE_TYPE_COMBOBOX, addEditPane, 1, 1, 1, 1, CLASS_CSG_LABEL, DISABLED, ENABLED).setPromptText("Options");
        csgBuilder.buildLabel(SCHEDULE_DATE_LABEL, addEditPane, 0, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildDatePicker(SCHEDULE_DATE_PICKER, addEditPane,1, 2, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_TITLE_LABEL, addEditPane, 0, 3, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SCHEDULE_TITLE_TEXT_FIELD, addEditPane, 1, 3, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_TOPIC_LABEL, addEditPane, 0, 4, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SCHEDULE_TOPIC_TEXT_FIELD, addEditPane, 1, 4, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildLabel(SCHEDULE_LINK_LABEL, addEditPane, 0, 5, 1, 1, CLASS_CSG_LABEL, ENABLED);
        csgBuilder.buildTextField(SCHEDULE_LINK_TEXT_FIELD, addEditPane, 1, 5, 1, 1, CLASS_CSG_LABEL, ENABLED);
        Button btnAddUpdate = csgBuilder.buildTextButton(SCHEDULE_ADD_UPDATE_BUTTON, addEditPane, 0, 6, 3, 3, CLASS_CSG_LABEL, ENABLED);
        Button btnClear = csgBuilder.buildTextButton(SCHEDULE_CLEAR_BUTTON, addEditPane, 1, 6, 3, 3, CLASS_CSG_LABEL, ENABLED);
        
        btnAddUpdate.prefWidthProperty().bindBidirectional(btnClear.prefWidthProperty());
        
        scheduleTab.setContent(schedulePane);
    }

    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }
    
    public void initControllers() {
        OfficeHoursController ohConrtoller = new OfficeHoursController((CSGApp) app);
        SiteController siteController = new SiteController((CSGApp) app);
        MTController mtController = new MTController((CSGApp) app);
        ScheduleController scheduleController = new ScheduleController((CSGApp) app);
        SyllabusController syllabusController = new SyllabusController((CSGApp) app);
        AppGUIModule gui = app.getGUIModule();
        
        initSiteController(siteController, gui);
        initSyllabusController(syllabusController, gui);
        initOHController(ohConrtoller, gui);
        initMTController(mtController, gui);
        initScheduleController(scheduleController, gui);
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new OfficeHoursFoolproofDesign((CSGApp) app));
        foolproofSettings.registerModeSettings(SCHEDULE_FOOLPROOF_SETTINGS,
                new ScheduleFoolproofDesign((CSGApp) app));
        foolproofSettings.registerModeSettings(SITE_FOOLPROOF_SETTINGS,
                new SiteFoolproofDesign((CSGApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    private void initOHController(OfficeHoursController ohController, AppGUIModule gui) {
        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            ohController.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            ohController.processTypeTA();
        });

        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            ohController.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            ohController.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            ohController.processAddTA();
        });

        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            ohController.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                ohController.processEditTA();
            }
            ohController.processSelectTA();
        });
        
        Button taRemoveButton = (Button) gui.getGUINode(OH_TA_REMOVE_BUTTON);
        taRemoveButton.setOnAction(e -> {
            if (!tasTableView.getSelectionModel().getSelectedItems().isEmpty()){
                ohController.processRemoveTA();
            }
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            ohController.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            ohController.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            ohController.processSelectUndergradTAs();
        });
        
        ComboBox<String> startHourBox = (ComboBox) gui.getGUINode(OH_START_TIME_COMBO_BOX);
        ComboBox<String> endHourBox = (ComboBox) gui.getGUINode(OH_END_TIME_COMBO_BOX);
        startHourBox.setOnHiding(e -> {
            if (startHourBox.getValue() != null && 
                    !startHourBox.getValue().isEmpty()){
                ohController.processSelectTime(startHourBox.getValue(), endHourBox.getValue());
            }
        });
        
        endHourBox.setOnHiding(e -> {
            if (startHourBox.getValue() != null && 
                    !startHourBox.getValue().isEmpty()){
                ohController.processSelectTime(startHourBox.getValue(), endHourBox.getValue());
            }
        });
    }

    private void initSyllabusController(SyllabusController syllabusController, AppGUIModule gui){
        TextArea descTextArea = (TextArea)gui.getGUINode(SYLL_DESC_TEXT_AREA);
        descTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(descTextArea, oldValue, newValue);
        });
        
        TextArea topicsTextArea = (TextArea)gui.getGUINode(SYLL_TOPICS_TEXT_AREA);
        topicsTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(topicsTextArea, oldValue, newValue);
        });
        
        TextArea prereqTextArea = (TextArea)gui.getGUINode(SYLL_PREREQ_TEXT_AREA);
        prereqTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(prereqTextArea, oldValue, newValue);
        });
        
        TextArea outcomesTextArea = (TextArea)gui.getGUINode(SYLL_OUTCOMES_TEXT_AREA);
        outcomesTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(outcomesTextArea, oldValue, newValue);
        });
        
        TextArea textbooksTextArea = (TextArea)gui.getGUINode(SYLL_TEXTBOOKS_TEXT_AREA);
        textbooksTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(textbooksTextArea, oldValue, newValue);
        });
        
        TextArea gradeCompTextArea = (TextArea)gui.getGUINode(SYLL_GRADE_COMP_TEXT_AREA);
        gradeCompTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(gradeCompTextArea, oldValue, newValue);
        });
        
        TextArea gradeNoteTextArea = (TextArea)gui.getGUINode(SYLL_GRADE_NOTE_TEXT_AREA);
        gradeNoteTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(gradeNoteTextArea, oldValue, newValue);
        });
        
        TextArea adTextArea = (TextArea)gui.getGUINode(SYLL_AD_TEXT_AREA);
        adTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(adTextArea, oldValue, newValue);
        });
        
        TextArea saTextArea = (TextArea)gui.getGUINode(SYLL_SA_TEXT_AREA);
        saTextArea.textProperty().addListener((ov, oldValue, newValue) -> {
            syllabusController.processChangeTextArea(saTextArea, oldValue, newValue);
        });
    }
    
    private void initSiteController(SiteController siteController, AppGUIModule gui) {
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SITE_SUBJECT_COMBOBOX);
        ComboBox<String> numberBox = (ComboBox)gui.getGUINode(SITE_NUMBER_COMBOBOX);
        ComboBox<String> semesterBox = (ComboBox)gui.getGUINode(SITE_SEMESTER_COMBOBOX);
        ComboBox<String> yearBox = (ComboBox)gui.getGUINode(SITE_YEAR_COMBOBOX);
        
        subjectBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                siteController.processComboAdd(subjectBox);
                subjectBox.getSelectionModel().clearSelection();
            }
        });
        subjectBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processComboChange(subjectBox, oldValue, newValue);
            siteController.updateDir();
        });
        
        numberBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                siteController.processComboAdd(numberBox);
                numberBox.getSelectionModel().clearSelection();
            }
        });
        numberBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processComboChange(numberBox, oldValue, newValue);
            siteController.updateDir();
        });
        
        semesterBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                siteController.processComboAdd(semesterBox);
                semesterBox.getSelectionModel().clearSelection();
            }
        });
        semesterBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processComboChange(semesterBox, oldValue, newValue);
            siteController.updateDir();
        });
        
        yearBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                siteController.processComboAdd(yearBox);
                yearBox.getSelectionModel().clearSelection();
            }
        });
        yearBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processComboChange(yearBox, oldValue, newValue);
            siteController.updateDir();
        });
        
        TextField titleField = (TextField)gui.getGUINode(SITE_TITLE_TEXT_FIELD);
        titleField.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextField(titleField, oldValue, newValue);
        });
        
        Button favButton = (Button)gui.getGUINode(SITE_FAV_ICON_BUTTON);
        ImageView favIcon = (ImageView)gui.getGUINode(SITE_FAV_ICON);
        favButton.setOnAction(eh -> {
            siteController.processChangeImage(favIcon, LogoType.FAVICON);
        });
        
        Button navButton = (Button)gui.getGUINode(SITE_NAV_BAR_IMAGE_BUTTON);
        ImageView navIcon = (ImageView)gui.getGUINode(SITE_NAV_BAR_ICON);
        navButton.setOnAction(eh -> {
            siteController.processChangeImage(navIcon, LogoType.NAVBAR);
        });
        
        Button lfButton = (Button)gui.getGUINode(SITE_LEFT_FOOTER_IMAGE_BUTTON);
        ImageView lfIcon = (ImageView)gui.getGUINode(SITE_LEFT_FOOT_ICON);
        lfButton.setOnAction(eh -> {
            siteController.processChangeImage(lfIcon, LogoType.BOTTOMLEFT);
        });
        
        Button rfButton = (Button)gui.getGUINode(SITE_RIGHT_FOOTER_IMAGE_BUTTON);
        ImageView rfIcon = (ImageView)gui.getGUINode(SITE_RIGHT_FOOT_ICON);
        rfButton.setOnAction(eh -> {
            siteController.processChangeImage(rfIcon, LogoType.BOTTOMRIGHT);
        });
        
        ComboBox<String> styleSheetBox = (ComboBox)gui.getGUINode(SITE_STYLE_SHEET_COMBOBOX);
        styleSheetBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processComboChange(styleSheetBox, oldValue, newValue);
        });
        
        TextField nameField = (TextField)gui.getGUINode(SITE_NAME_TEXT_FIELD);
        nameField.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextField(nameField, oldValue, newValue);
        });
        
        TextField roomField = (TextField)gui.getGUINode(SITE_ROOM_TEXT_FIELD);
        roomField.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextField(roomField, oldValue, newValue);
        });
        
        TextField emailField = (TextField)gui.getGUINode(SITE_EMAIL_TEXT_FIELD);
        emailField.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextField(emailField, oldValue, newValue);
        });
        
        TextField hpField = (TextField)gui.getGUINode(SITE_HOMEPAGE_TEXT_FIELD);
        hpField.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextField(hpField, oldValue, newValue);
        });
        
        TextArea ohArea = (TextArea)gui.getGUINode(SITE_OFFICE_HOURS_TEXT_AREA);
        ohArea.textProperty().addListener((ov, oldValue, newValue) -> {
            siteController.processChangeTextArea(ohArea, oldValue, newValue);
        });
        
        CheckBox cbHome = (CheckBox)gui.getGUINode(SITE_HOME_CHECKBOX);
        cbHome.setOnMouseClicked(eh -> {
            siteController.processCheck(cbHome);
        });
        CheckBox cbSyllabus = (CheckBox)gui.getGUINode(SITE_SYLLABUS_CHECKBOX);
        cbSyllabus.setOnAction(eh -> {
            siteController.processCheck(cbSyllabus);
        });
        CheckBox cbSchedule = (CheckBox)gui.getGUINode(SITE_SCHEDULE_CHECKBOX);
        cbSchedule.setOnAction(eh -> {
            siteController.processCheck(cbSchedule);
        });
        CheckBox cbHWs = (CheckBox)gui.getGUINode(SITE_HWS_CHECKBOX);
        cbHWs.setOnAction(eh -> {
            siteController.processCheck(cbHWs);
        });
        
    }

    private void initMTController(MTController mtController, AppGUIModule gui) {
        TableView tblLecture = (TableView)gui.getGUINode(MT_LECTURE_TABLE_VIEW);
        
        // add lecture
        Button btnAddLecture = (Button)gui.getGUINode(MT_ADD_LECTURE_BUTTON);
        btnAddLecture.setOnAction(eh -> {
            mtController.processAddLecture();
        });
        
        // remove lecture
        Button btnRemoveLecture = (Button)gui.getGUINode(MT_REMOVE_LECTURE_BUTTON);
        btnRemoveLecture.setOnAction(eh -> {
            if (!tblLecture.getSelectionModel().getSelectedItems().isEmpty()){
                mtController.processRemoveLecture();
            }
        });
        
        TableView tblRec = (TableView)gui.getGUINode(MT_REC_TABLE_VIEW);
        
        Button btnAddRec = (Button)gui.getGUINode(MT_ADD_REC_BUTTON);
        btnAddRec.setOnAction(eh -> {
            mtController.processAddRec();
        });
        
        Button btnRemoveRec = (Button)gui.getGUINode(MT_REMOVE_REC_BUTTON);
        btnRemoveRec.setOnAction(eh -> {
            if (!tblRec.getSelectionModel().getSelectedItems().isEmpty()){
                mtController.processRemoveRec();
            }
        });
        
        TableView tblLab = (TableView)gui.getGUINode(MT_LAB_TABLE_VIEW);
        
        Button btnAddLab = (Button)gui.getGUINode(MT_ADD_LAB_BUTTON);
        btnAddLab.setOnAction(eh -> {
            mtController.processAddLab();
        });
        
        Button btnRemoveLab = (Button)gui.getGUINode(MT_REMOVE_LAB_BUTTON);
        btnRemoveLab.setOnAction(eh -> {
            if (!tblLab.getSelectionModel().getSelectedItems().isEmpty()){
                mtController.processRemoveLab();
            }
        });
    }

    private void initScheduleController(ScheduleController scheduleController, AppGUIModule gui) {
        DatePicker dpStartMon = (DatePicker)gui.getGUINode(SCHEDULE_START_MON_PICKER);
        dpStartMon.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeDate(dpStartMon, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        DatePicker dpEndFri = (DatePicker)gui.getGUINode(SCHEDULE_END_FRI_PICKER);
        dpEndFri.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeDate(dpEndFri, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        ComboBox<String> subjectBox = (ComboBox)gui.getGUINode(SCHEDULE_TYPE_COMBOBOX);
        subjectBox.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeType(subjectBox, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        DatePicker dpDate = (DatePicker)gui.getGUINode(SCHEDULE_DATE_PICKER);
        dpDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeDate(dpDate, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        TextField title = (TextField)gui.getGUINode(SCHEDULE_TITLE_TEXT_FIELD);
        title.textProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeTextField(title, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        TextField topic = (TextField)gui.getGUINode(SCHEDULE_TOPIC_TEXT_FIELD);
        topic.textProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeTextField(topic, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        TextField link = (TextField)gui.getGUINode(SCHEDULE_LINK_TEXT_FIELD);
        link.textProperty().addListener((ov, oldValue, newValue) -> {
            if (!app.getFileModule().isUndoed()) scheduleController.processChangeTextField(link, oldValue, newValue);
            app.getFileModule().markAsUndoed(DISABLED);
        });
        
        Button addUpdateSchedule = (Button)gui.getGUINode(SCHEDULE_ADD_UPDATE_BUTTON);
        addUpdateSchedule.setOnAction(eh -> {
            scheduleController.processAddUpdateSchedule();
        });
        
        Button clearSchedule = (Button)gui.getGUINode(SCHEDULE_CLEAR_BUTTON);
        clearSchedule.setOnAction(eh -> {
            scheduleController.processClearSchedule();
        });
        
        TableView tblSchedule = (TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        tblSchedule.setOnMouseClicked(eh -> {
            scheduleController.processToggleSchedule();
        });
        
        Button removeSchedule = (Button)gui.getGUINode(SCHEDULE_REMOVE_BUTTON);
        removeSchedule.setOnAction(eh -> {
            if (!tblSchedule.getSelectionModel().getSelectedItems().isEmpty()){
                scheduleController.processRemoveSchedule();
            }
        });
    }

}
