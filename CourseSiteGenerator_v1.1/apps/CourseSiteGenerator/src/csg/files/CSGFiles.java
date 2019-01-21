package csg.files;

import org.apache.commons.io.FileUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CSGApp;
import static csg.CSGPropertyType.SITE_EXPORTDIR;
import csg.data.CSGData;
import csg.data.Lecture;
import csg.data.MTData;
import csg.data.OfficeHoursData;
import csg.data.ScheduleData;
import csg.data.ScheduleData.ScheduleType;
import csg.data.SchedulePrototype;
import csg.data.SiteData;
import csg.data.SubLecture;
import csg.data.SyllabusData;
import csg.data.TAType;
import csg.data.TeachingAssistantPrototype;
import csg.data.TimeSlot;
import csg.data.TimeSlot.DayOfWeek;
import static djf.AppPropertyType.APP_PATH_EXPORT;
import static djf.AppTemplate.PATH_WORK;
import java.io.File;
import java.io.StringReader;
import java.time.LocalDate;
import javafx.scene.control.Label;
import javax.json.JsonObjectBuilder;
import properties_manager.PropertiesManager;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class CSGFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CSGApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    // COURSE SITE GENERATOR JSON TYPES
    static final String JSON_SUBJECTS = "subjects";
    static final String JSON_NUMBERS = "numbers";
    static final String JSON_SEMESTERS = "semesters";
    static final String JSON_YEARS = "years";
    
    // SITE JSONTYPES
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_LOGOS = "logos";
    static final String JSON_FAVICON = "favicon";
    static final String JSON_NAVBAR = "navbar";
    static final String JSON_BOTTOM_LEFT = "bottom_left";
    static final String JSON_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_HREF = "href";
    static final String JSON_SRC = "src";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_LINK = "link";
    static final String JSON_ROOM = "room";
    static final String JSON_PHOTO = "photo";
    static final String JSON_HOURS = "hours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_PAGES = "pages";
    
    // SYLLABUS TYPES
    static final String JSON_DESC = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_PREREQ = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_GRADED_COMPONENTS = "gradedComponents";
    static final String JSON_GRADINGNOTE = "gradingNote";
    static final String JSON_ACADEMIC_DISHONESTY = "academicDishonesty";
    static final String JSON_SPECIAL_ASSISTANCE = "specialAssistance";
    static final String JSON_WEIGHT = "weight";
    static final String JSON_AUTHORS = "authors";
    static final String JSON_PUBLISHER = "publisher";
    
    // SECTION TYPES
    static final String JSON_LECTURES = "lectures";
    static final String JSON_LABS = "labs";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_DAY_TIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";
    
    // OH JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    
    // SCHEDULE JSON TYPE
    static final String JSON_START_MON_MONTH = "startingMondayMonth";
    static final String JSON_START_MON_DAY = "startingMondayDay";
    static final String JSON_END_FRI_MONTH = "endingFridayMonth";
    static final String JSON_END_FRI_DAY = "endingFridayDay";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_REFERENCES = "references";
    static final String JSON_HWS = "hws";
    static final String JSON_MONTH = "month";
    static final String JSON_TOPIC = "topic";

    public CSGFiles(CSGApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGData dataManager = (CSGData) data;
        //loadCSGData(dataManager);
	
        loadSiteData(dataManager.getSiteData(), filePath);
        loadSyllabusData(dataManager.getSyllabusData(), filePath);
        loadMTData(dataManager.getMTData(), filePath);
        loadOHData(dataManager.getOHData(), filePath);
        loadScheduleData(dataManager.getScheduleData(), filePath);
        app.getTPS().clearAllTransactions();
    }
    
    public void loadCSGData(CSGData dataManager) throws IOException{
        JsonObject generatorJson = loadJSONFile(PATH_WORK+"Config.json");
        
        JsonArray jsonSubjectArray = generatorJson.getJsonArray(JSON_SUBJECTS);
        jsonSubjectArray.forEach((subject) -> {
            dataManager.addSubject(subject.toString().replaceAll("\"", ""));
        });
        
        JsonArray jsonNumberArray = generatorJson.getJsonArray(JSON_NUMBERS);
        jsonNumberArray.forEach((number) -> {
            dataManager.addNumber(number.toString().replaceAll("\"", ""));
        });
        
        JsonArray jsonSemesterArray = generatorJson.getJsonArray(JSON_SEMESTERS);
        jsonSemesterArray.forEach((semester) -> {
            dataManager.addSemester(semester.toString().replaceAll("\"", ""));
        });
        
        JsonArray jsonYearArray = generatorJson.getJsonArray(JSON_YEARS);
        jsonYearArray.forEach((year) -> {
            dataManager.addYear(year.toString().replaceAll("\"", ""));
        });
    }
    
    private void loadSiteData(SiteData dataManager, String filePath) throws IOException {
	// LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject siteJson = loadJSONFile(filePath+"/PageData.json");

	// LOAD THE START AND END HOURS
	String subject = siteJson.getString(JSON_SUBJECT);
        String number = siteJson.getString(JSON_NUMBER);
        String semester = siteJson.getString(JSON_SEMESTER);
        String year = siteJson.getString(JSON_YEAR);
        String title = siteJson.getString(JSON_TITLE);
        dataManager.setSiteInfo(subject, number, semester, year, title);
        
        JsonObject jsonLogoObject = siteJson.getJsonObject(JSON_LOGOS);
        JsonObject favicon = jsonLogoObject.getJsonObject(JSON_FAVICON);
        dataManager.setHref(favicon.getString(JSON_HREF), SiteData.LogoType.FAVICON);
        //dataManager.setLogoInfo(favicon.getString(JSON_SRC), SiteData.LogoType.FAVICON);
        
        JsonObject navbar = jsonLogoObject.getJsonObject(JSON_NAVBAR);
        dataManager.setHref(navbar.getString(JSON_HREF), SiteData.LogoType.NAVBAR);
        dataManager.setLogoInfo(navbar.getString(JSON_SRC), SiteData.LogoType.NAVBAR);
        
        JsonObject bottomLeft = jsonLogoObject.getJsonObject(JSON_BOTTOM_LEFT);
        dataManager.setHref(bottomLeft.getString(JSON_HREF), SiteData.LogoType.BOTTOMLEFT);
        dataManager.setLogoInfo(bottomLeft.getString(JSON_SRC), SiteData.LogoType.BOTTOMLEFT);
        
        JsonObject bottomRight = jsonLogoObject.getJsonObject(JSON_BOTTOM_RIGHT);
        dataManager.setHref(bottomRight.getString(JSON_HREF), SiteData.LogoType.BOTTOMRIGHT);
        dataManager.setLogoInfo(bottomRight.getString(JSON_SRC), SiteData.LogoType.BOTTOMRIGHT);
        dataManager.updateLogos();
        
        JsonObject instructorObject = siteJson.getJsonObject(JSON_INSTRUCTOR);
        
        String name = instructorObject.getString(JSON_NAME);
        String link = instructorObject.getString(JSON_LINK);
        String email = instructorObject.getString(JSON_EMAIL);
        String room = instructorObject.getString(JSON_ROOM);
        String photo = instructorObject.getString(JSON_PHOTO);
        String hours = instructorObject.getJsonArray(JSON_HOURS).toString();
        dataManager.setInstructorInfo(name, link, email, room, photo, hours);
        
        JsonArray pages = siteJson.getJsonArray(JSON_PAGES);
        for (int i = 0; i < pages.size(); i++){
            dataManager.togglePage((pages.getJsonObject(i)).getString(JSON_NAME));
        }
    }
    
    private void loadSyllabusData(SyllabusData dataManager, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath+"/SyllabusData.json");
        String desc = json.getString(JSON_DESC);
        String topics = json.getJsonArray(JSON_TOPICS).toString();
        String prereq = json.getString(JSON_PREREQ);
        String outcome = json.getJsonArray(JSON_OUTCOMES).toString();
        String textbook = json.getJsonArray(JSON_TEXTBOOKS).toString();
        String gradeComp = json.getJsonArray(JSON_GRADED_COMPONENTS).toString();
        String gradeNote = json.getString(JSON_GRADINGNOTE);
        String academicDishonesty = json.getString(JSON_ACADEMIC_DISHONESTY);
        String specialAssistance = json.getString(JSON_SPECIAL_ASSISTANCE);
        dataManager.initData(desc, topics, prereq, outcome, textbook, gradeComp, gradeNote, academicDishonesty, specialAssistance);
    }
    
    private void loadMTData(MTData dataManager, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath+"/SectionsData.json");
        JsonArray lectures = json.getJsonArray(JSON_LECTURES);
        for (int i = 0; i < lectures.size(); i++) {
            JsonObject jsonLecture = lectures.getJsonObject(i);
            String section = jsonLecture.getString(JSON_SECTION);
            String days = jsonLecture.getString(JSON_DAYS);
            days = dataManager.extractDay(days);
            String time = jsonLecture.getString(JSON_TIME);
            String room = jsonLecture.getString(JSON_ROOM);
            Lecture lecture = new Lecture(section, days, time, room);
            dataManager.addLecture(lecture);
        }
        
        JsonArray labs = json.getJsonArray(JSON_LABS);
        for (int i = 0; i < labs.size(); i++) {
            JsonObject jsonLab = labs.getJsonObject(i);
            String section = jsonLab.getString(JSON_SECTION);
            section = dataManager.stripSection(section);
            String dayTime = jsonLab.getString(JSON_DAY_TIME);
            String day = dataManager.extractDay(dayTime);
            String time = dataManager.extractTime(dayTime);
            String location = jsonLab.getString(JSON_LOCATION);
            String ta1 = jsonLab.getString(JSON_TA1);
            String ta2 = jsonLab.getString(JSON_TA2);
            SubLecture lab = new SubLecture(section, day, time, location, ta1, ta2);
            dataManager.addLab(lab);
        }
        
        JsonArray recitations = json.getJsonArray(JSON_RECITATIONS);
        for (int i = 0; i < recitations.size(); i++) {
            JsonObject jsonRecitation = recitations.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_SECTION);
            section = dataManager.stripSection(section);
            String dayTime = jsonRecitation.getString(JSON_DAY_TIME);
            String day = dataManager.extractDay(dayTime);
            String time = dataManager.extractTime(dayTime);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String ta1 = jsonRecitation.getString(JSON_TA1);
            String ta2 = jsonRecitation.getString(JSON_TA2);
            SubLecture rec = new SubLecture(section, day, time, location, ta1, ta2);
            dataManager.addRecitation(rec);
        }
    }
    
    private void loadOHData(OfficeHoursData dataManager, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath+"/OfficeHoursData.json");

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
        }
    }
    
    private void loadScheduleData(ScheduleData dataManager, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath+"/ScheduleData.json");
        String startMonMonth = json.getString(JSON_START_MON_MONTH);
        String startMonDay = json.getString(JSON_START_MON_DAY);
        LocalDate startMon = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(startMonMonth), Integer.parseInt(startMonDay));
        
        String endFriMonth = json.getString(JSON_END_FRI_MONTH);
        String endFriDay = json.getString(JSON_END_FRI_DAY);
        LocalDate endFri = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(endFriMonth), Integer.parseInt(endFriDay));
        
        dataManager.setStartMon(startMon);
        dataManager.setEndFri(endFri);
        
        JsonArray holidays = json.getJsonArray(JSON_HOLIDAYS);
        for (int i = 0; i < holidays.size(); i++) {
            JsonObject holiday = holidays.getJsonObject(i);
            String month = holiday.getString(JSON_MONTH);
            String day = holiday.getString(JSON_DAY);
            String title = holiday.getString(JSON_TITLE);
            String link = holiday.getString(JSON_LINK);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
            SchedulePrototype schedule = new SchedulePrototype(date, title, "", ScheduleType.Holiday, link);
            dataManager.addSchedule(schedule);
        }
        
        JsonArray lectures = json.getJsonArray(JSON_LECTURES);
        for (int i = 0; i < lectures.size(); i++) {
            JsonObject lecture = lectures.getJsonObject(i);
            String month = lecture.getString(JSON_MONTH);
            String day = lecture.getString(JSON_DAY);
            String title = lecture.getString(JSON_TITLE);
            String topic = lecture.getString(JSON_TOPIC);
            String link = lecture.getString(JSON_LINK);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
            SchedulePrototype schedule = new SchedulePrototype(date, title, topic, ScheduleType.Lecture, link);
            dataManager.addSchedule(schedule);
        }
        
        JsonArray references = json.getJsonArray(JSON_REFERENCES);
        for (int i = 0; i < references.size(); i++) {
            JsonObject reference = references.getJsonObject(i);
            String month = reference.getString(JSON_MONTH);
            String day = reference.getString(JSON_DAY);
            String title = reference.getString(JSON_TITLE);
            String topic = reference.getString(JSON_TOPIC);
            String link = reference.getString(JSON_LINK);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
            SchedulePrototype schedule = new SchedulePrototype(date, title, topic, ScheduleType.Reference, link);
            dataManager.addSchedule(schedule);
        }
        
        JsonArray recitations = json.getJsonArray(JSON_RECITATIONS);
        for (int i = 0; i < recitations.size(); i++) {
            JsonObject recitation = recitations.getJsonObject(i);
            String month = recitation.getString(JSON_MONTH);
            String day = recitation.getString(JSON_DAY);
            String title = recitation.getString(JSON_TITLE);
            String topic = recitation.getString(JSON_TOPIC);
            String link = recitation.getString(JSON_LINK);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
            SchedulePrototype schedule = new SchedulePrototype(date, title, topic, ScheduleType.Recitation, link);
            dataManager.addSchedule(schedule);
        }
        
        JsonArray hws = json.getJsonArray(JSON_HWS);
        for (int i = 0; i < hws.size(); i++) {
            JsonObject hw = hws.getJsonObject(i);
            String month = hw.getString(JSON_MONTH);
            String day = hw.getString(JSON_DAY);
            String title = hw.getString(JSON_TITLE);
            String topic = hw.getString(JSON_TOPIC);
            String link = hw.getString(JSON_LINK);
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(month), Integer.parseInt(day));
            SchedulePrototype schedule = new SchedulePrototype(date, title, topic, ScheduleType.HW, link);
            dataManager.addSchedule(schedule);
        }
    }
    
    
    private void loadTAs(OfficeHoursData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = (tas == JSON_GRAD_TAS)? TAType.Graduate : TAType.Undergraduate;
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // SAVE GENERATOR SETTINGS HERE USING SITE DATA
        CSGData dataManager = (CSGData) data;
        
        JsonArrayBuilder jsonSubjectBuilder = Json.createArrayBuilder();
        Iterator<String> subjects = dataManager.subjectsIterator();
        while(subjects.hasNext()) {
            jsonSubjectBuilder.add(subjects.next());
        };
        JsonArray jsonSubjectArray = jsonSubjectBuilder.build();
        
        JsonArrayBuilder jsonNumberBuilder = Json.createArrayBuilder();
        Iterator<String> numbers = dataManager.numbersIterator();
        while(numbers.hasNext()) {
            jsonNumberBuilder.add(numbers.next());
        };
        JsonArray jsonNumberArray = jsonNumberBuilder.build();
        
        JsonArrayBuilder jsonSemesterBuilder = Json.createArrayBuilder();
        Iterator<String> semester = dataManager.semestersIterator();
        while(semester.hasNext()) {
            jsonSemesterBuilder.add(semester.next());
        };
        JsonArray jsonSemesterArray = jsonSemesterBuilder.build();
        
        JsonArrayBuilder jsonYearBuilder = Json.createArrayBuilder();
        Iterator<String> year = dataManager.yearsIterator();
        while(year.hasNext()) {
            jsonYearBuilder.add(year.next());
        };
        JsonArray jsonYearArray = jsonYearBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_SUBJECTS, jsonSubjectArray)
                .add(JSON_NUMBERS, jsonNumberArray)
                .add(JSON_SEMESTERS, jsonSemesterArray)
                .add(JSON_YEARS, jsonYearArray)
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(PATH_WORK+"Config.json", false);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(PATH_WORK+"Config.json");
	pw.write(prettyPrinted);
	pw.close();
        
	saveSiteData(dataManager.getSiteData(), filePath + "/PageData.json");
        saveSyllabusData(dataManager.getSyllabusData(), filePath + "/SyllabusData.json");
	saveOHData(dataManager.getOHData(), filePath + "/OfficeHoursData.json");
        saveMTData(dataManager, filePath + "/SectionsData.json");
        saveScheduleData(dataManager.getScheduleData(), filePath + "/ScheduleData.json");
    }
    
    private void saveSiteData(SiteData siteData, String filePath) throws IOException {
        // Create Logo Object
        JsonObject logoJson = Json.createObjectBuilder()
                .add(JSON_FAVICON, Json.createObjectBuilder()
                        .add(JSON_HREF, siteData.getHref(SiteData.LogoType.FAVICON)).build())
                .add(JSON_NAVBAR, Json.createObjectBuilder()
                        .add(JSON_HREF, siteData.getHref(SiteData.LogoType.NAVBAR))
                        .add(JSON_SRC, siteData.getLogoInfo(SiteData.LogoType.NAVBAR)).build())
                .add(JSON_BOTTOM_LEFT, Json.createObjectBuilder()
                        .add(JSON_HREF, siteData.getHref(SiteData.LogoType.BOTTOMLEFT))
                        .add(JSON_SRC, siteData.getLogoInfo(SiteData.LogoType.BOTTOMLEFT)).build())
                .add(JSON_BOTTOM_RIGHT, Json.createObjectBuilder()
                        .add(JSON_HREF, siteData.getHref(SiteData.LogoType.BOTTOMRIGHT))
                        .add(JSON_SRC, siteData.getLogoInfo(SiteData.LogoType.BOTTOMRIGHT)).build())
                .build();
        //JsonReader jsonReader = Json.createReader(new StringReader(siteData.getInstructorOfficeHours()));
        //JsonArray ohArray = jsonReader.readArray();
        
        JsonObject instructorJson = Json.createObjectBuilder()
                .add(JSON_NAME, siteData.getInstructorName())
                .add(JSON_LINK, siteData.getInstructorHomepage())
                .add(JSON_EMAIL, siteData.getInstructorEmail())
                .add(JSON_ROOM, siteData.getInstructorRoom())
                .add(JSON_PHOTO, siteData.getInstructorPhoto())
                .add(JSON_HOURS, Json.createReader(new StringReader(siteData.getInstructorOfficeHours())).readArray())
                .build();
        
        JsonArrayBuilder pageJson = Json.createArrayBuilder();
        if (siteData.isPage("Home")){
            pageJson.add(Json.createObjectBuilder()
                .add(JSON_NAME, "Home")
                .add(JSON_LINK, "index.html").build());
        }
        if (siteData.isPage("Syllabus")){
            pageJson.add(Json.createObjectBuilder()
                .add(JSON_NAME, "Syllabus")
                .add(JSON_LINK, "syllabus.html").build());
        }
        if (siteData.isPage("Schedule")){
            pageJson.add(Json.createObjectBuilder()
                .add(JSON_NAME, "Schedule")
                .add(JSON_LINK, "schedule.html").build());
        }
        if (siteData.isPage("HWs")){
            pageJson.add(Json.createObjectBuilder()
                .add(JSON_NAME, "HWs")
                .add(JSON_LINK, "hws.html").build());
        }
        
        // THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_SUBJECT, siteData.getSubject())
		.add(JSON_NUMBER, siteData.getNumber())
                .add(JSON_SEMESTER, siteData.getSemester())
                .add(JSON_YEAR, siteData.getYear())
                .add(JSON_TITLE, siteData.getTitle())
                .add(JSON_LOGOS, logoJson)
                .add(JSON_INSTRUCTOR, instructorJson)
                .add(JSON_PAGES, pageJson.build())
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void saveSyllabusData(SyllabusData syllabusData, String filePath) throws IOException {
        JsonObject syllabusJson = Json.createObjectBuilder()
                .add(JSON_DESC, syllabusData.getDescription())
                .add(JSON_TOPICS, Json.createReader(new StringReader(syllabusData.getTopics())).readArray())
                .add(JSON_PREREQ, syllabusData.getPrerequisites())
                .add(JSON_OUTCOMES, Json.createReader(new StringReader(syllabusData.getOutcomes())).readArray())
                .add(JSON_TEXTBOOKS, Json.createReader(new StringReader(syllabusData.getTextbooks())).readArray())
                .add(JSON_GRADED_COMPONENTS, Json.createReader(new StringReader(syllabusData.getGradedComponents())).readArray())
                .add(JSON_GRADINGNOTE, syllabusData.getGradingNote())
                .add(JSON_ACADEMIC_DISHONESTY, syllabusData.getAcademicDishonesty())
                .add(JSON_SPECIAL_ASSISTANCE, syllabusData.getSpecialAssistance())
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(syllabusJson);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(syllabusJson);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        
    }
    
    private void saveOHData(OfficeHoursData dataManager, String filePath) throws IOException {
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJCTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void saveMTData(CSGData dataManager, String filePath) throws IOException {
        MTData mtData = dataManager.getMTData();
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
	Iterator<Lecture> lectureIterator = mtData.lecturesIterator();
        while (lectureIterator.hasNext()) {
            Lecture lecture = lectureIterator.next();
            JsonObject lectureJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, lecture.getSection())
                    .add(JSON_DAYS, mtData.getDays(lecture.getDay()))
                    .add(JSON_TIME, lecture.getTime())
                    .add(JSON_ROOM, lecture.getRoom())
                    .build();
            lecturesArrayBuilder.add(lectureJson);
        }
	
        JsonArrayBuilder labsArrayBuilder = Json.createArrayBuilder();
	Iterator<SubLecture> labIterator = mtData.labIterator();
        while (labIterator.hasNext()) {
            SubLecture lab = labIterator.next();
            JsonObject lectureJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mtData.wrapSection(lab.getSection(), dataManager.getSiteData().getInstructorName()))
                    .add(JSON_DAY_TIME, mtData.getDayTime(lab))
                    .add(JSON_LOCATION, lab.getRoom())
                    .add(JSON_TA1, lab.getTa1())
                    .add(JSON_TA2, lab.getTa2())
                    .build();
            labsArrayBuilder.add(lectureJson);
        }
        
        
        JsonArrayBuilder recsArrayBuilder = Json.createArrayBuilder();
	Iterator<SubLecture> recIterator = mtData.recIterator();
        while (recIterator.hasNext()) {
            SubLecture rec = recIterator.next();
            JsonObject recJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, mtData.wrapSection(rec.getSection(), dataManager.getSiteData().getInstructorName()))
                    .add(JSON_DAY_TIME, mtData.getDayTime(rec))
                    .add(JSON_LOCATION, rec.getRoom())
                    .add(JSON_TA1, rec.getTa1())
                    .add(JSON_TA2, rec.getTa2())
                    .build();
            recsArrayBuilder.add(recJson);
        }
        
        JsonArray lecturesArray = lecturesArrayBuilder.build();
        JsonArray labsArray = labsArrayBuilder.build();
        JsonArray recArray =recsArrayBuilder.build();
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_LECTURES, lecturesArray)
		.add(JSON_LABS, labsArray)
                .add(JSON_RECITATIONS, recArray)
		.build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private void saveScheduleData(ScheduleData dataManager, String filePath) throws IOException {
	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder holidaysArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecturesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwsArrayBuilder = Json.createArrayBuilder();
	Iterator<SchedulePrototype> scheduleIterator = dataManager.scheduleIterator();
        while (scheduleIterator.hasNext()) {
            SchedulePrototype schedule = scheduleIterator.next();
	    JsonObjectBuilder scheduleBuilder = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+schedule.getDate().getMonthValue())
		    .add(JSON_DAY, ""+schedule.getDate().getDayOfMonth())
		    .add(JSON_TITLE, schedule.getTitle());
            if (schedule.getType().equals(ScheduleType.Holiday.toString())){
                JsonObject scheduleJson  = scheduleBuilder.add(JSON_LINK, schedule.getLink()).build();
                holidaysArrayBuilder.add(scheduleJson);
            }else{
                JsonObject scheduleJson = scheduleBuilder.add(JSON_TOPIC, schedule.getTopic())
                        .add(JSON_LINK, schedule.getLink()).build();
                if (schedule.getType().equals(ScheduleType.Lecture.toString()))
                    lecturesArrayBuilder.add(scheduleJson);
                else if (schedule.getType().equals(ScheduleType.Holiday.toString()))
                    holidaysArrayBuilder.add(scheduleJson);
                else if (schedule.getType().equals(ScheduleType.Reference.toString()))
                    referencesArrayBuilder.add(scheduleJson);
                else if (schedule.getType().equals(ScheduleType.Recitation.toString()))
                    recitationsArrayBuilder.add(scheduleJson);
                else if (schedule.getType().equals(ScheduleType.HW.toString()))
                    hwsArrayBuilder.add(scheduleJson);
            }
            
	}
        JsonArray holidaysArray = holidaysArrayBuilder.build();
	JsonArray lecturesArray = lecturesArrayBuilder.build();
	JsonArray referencesArray = referencesArrayBuilder.build();
	JsonArray recitationsArray = recitationsArrayBuilder.build();
	JsonArray hwsArray = hwsArrayBuilder.build();

	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_MON_MONTH, "" + dataManager.getStartMon().getMonthValue())
		.add(JSON_START_MON_DAY, "" + dataManager.getStartMon().getDayOfMonth())
                .add(JSON_END_FRI_MONTH, "" + dataManager.getEndFri().getMonthValue())
		.add(JSON_END_FRI_DAY, "" + dataManager.getEndFri().getDayOfMonth())
                .add(JSON_HOLIDAYS, holidaysArray)
                .add(JSON_LECTURES, lecturesArray)
                .add(JSON_REFERENCES, referencesArray)
                .add(JSON_RECITATIONS, recitationsArray)
                .add(JSON_HWS, hwsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        File currentDir = app.getFileModule().getWorkFile();
        
        try {
            FileUtils.copyDirectory(currentDir, new File("./js"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        createExportDir();
    }
    
    private void createExportDir() throws IOException{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String export = props.getProperty(APP_PATH_EXPORT);
        String src = export + "page_template";
        Label dir = (Label)app.getGUIModule().getGUINode(SITE_EXPORTDIR);
        File exportFile = new File(dir.getText());
        exportFile.getParentFile().mkdir();
        
        try {
            FileUtils.copyDirectory(new File(src), exportFile.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        File currentDir = app.getFileModule().getWorkFile();
        
        try {
            FileUtils.copyDirectory(currentDir, new File(exportFile.getParent()+"/js"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}