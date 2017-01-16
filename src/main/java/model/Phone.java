package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class Phone implements Serializable {
    private int id;
    private int countryID;
    private int operatorCode;
    private long phoneNumber;
    private int contactID;
    private boolean type;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(int operatorCode) {
        this.operatorCode = operatorCode;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Phone(int id, int countryID, int operatorCode, long phoneNumber, int contactID, boolean type, String comment) {
        this.id = id;
        this.countryID = countryID;
        this.operatorCode = operatorCode;
        this.phoneNumber = phoneNumber;
        this.contactID = contactID;
        this.type = type;
        this.comment = comment;
    }

    public Phone() {
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
