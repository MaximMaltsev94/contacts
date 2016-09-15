package model;

import java.io.Serializable;

public class Region implements Serializable {
    private int id;
    private String name;
    private int countryID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public Region(int id, String name, int countryID) {
        this.id = id;
        this.name = name;
        this.countryID = countryID;
    }

    public Region() {
    }
}
