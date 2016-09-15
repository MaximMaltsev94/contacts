package model;

import java.io.Serializable;

public class City implements Serializable {
    private int id;
    private String name;
    private int regionID;

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

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public City(int id, String name, int regionID) {
        this.id = id;
        this.name = name;
        this.regionID = regionID;
    }

    public City() {
    }
}
