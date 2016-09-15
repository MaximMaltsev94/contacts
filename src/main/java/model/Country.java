package model;

import java.io.Serializable;

public class Country implements Serializable {
    private int id;
    private int phoneCode;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country(int id, int phoneCode, String name) {
        this.id = id;
        this.phoneCode = phoneCode;
        this.name = name;
    }

    public Country() {
    }
}
