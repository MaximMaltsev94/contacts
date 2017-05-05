package model;

import java.io.Serializable;

public class City extends GenericModel implements Serializable {
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

    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
