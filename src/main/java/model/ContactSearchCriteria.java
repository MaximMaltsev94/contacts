package model;

import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class ContactSearchCriteria {
    private String firstName;
    private String lastName;
    private String patronymic;
    private int age1;
    private int age2;
    private int gender;
    private String citizenship;
    private int relationship;
    private String companyName;
    private int country;
    private int city;
    private String street;
    private String postcode;

    public ContactSearchCriteria() {
    }

    public ContactSearchCriteria(HttpServletRequest request) {
        firstName = (String )request.getAttribute("firstName");
        lastName = (String) request.getAttribute("lastName");
        patronymic = (String)request.getAttribute("patronymic");
        age1 = Integer.parseInt((String) request.getAttribute("age1"));
        age2 = Integer.parseInt((String) request.getAttribute("age2"));
        gender = Integer.parseInt((String) request.getAttribute("gender"));
        citizenship = (String)request.getAttribute("citizenship");
        relationship = Integer.parseInt((String) request.getAttribute("relationship"));
        companyName = (String)request.getAttribute("companyName");
        country = Integer.parseInt((String) request.getAttribute("country"));
        city = Integer.parseInt((String) request.getAttribute("city"));
        street = (String)request.getAttribute("street");
        postcode = (String)request.getAttribute("postcode");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge1() {
        return age1;
    }

    public void setAge1(int age1) {
        this.age1 = age1;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
