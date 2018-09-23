package oh.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistantPrototype {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email;
    private final IntegerProperty slot;

    /**
     * Constructor initializes both the TA name and email.
     */
    public TeachingAssistantPrototype(String initName, String initEmail) {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
        slot = new SimpleIntegerProperty(0);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String initEmail) {
        email.set(initEmail);
    }
    
    public StringProperty emailProperty() {
        return email;
    }
    
    public IntegerProperty timeslotProperty(){
        return slot;
    }
    
    public int getSlot() {
        return slot.get();
    }

    public void setSlot(int initSlot) {
        slot.set(initSlot);
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
    

    // checks if another TA has the same name or email
    public boolean equals(TeachingAssistantPrototype other) {
        return name.get().equals(other.getName()) || email.get().equals(other.getEmail());
    }
}