/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.data;

import javafx.beans.property.StringProperty;

/**
 *
 * @author hanbin
 */
public class InstructorPrototype {
    private final StringProperty instructorName;
    private final StringProperty instructorRoom;
    private final StringProperty instructorEmail;
    private final StringProperty instructorHomepage;
    private final StringProperty instructorOfficeHours;
    
    public InstructorPrototype(StringProperty name, StringProperty room, StringProperty email, StringProperty homepage, StringProperty officeHours){
        instructorName = name;
        instructorRoom = room;
        instructorEmail = email;
        instructorHomepage = homepage;
        instructorOfficeHours = officeHours;
    }
}
