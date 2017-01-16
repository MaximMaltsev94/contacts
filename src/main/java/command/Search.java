package command;

import dao.implementation.*;
import dao.interfaces.CityDao;
import dao.interfaces.ContactDao;
import dao.interfaces.CountryDao;
import dao.interfaces.RelationshipDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.City;
import model.Contact;
import model.Country;
import model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Search implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(Search.class);

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

    private void parseRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        firstName = ContactUtils.getUTF8String(request.getParameter("firstName"));
        lastName = ContactUtils.getUTF8String(request.getParameter("lastName"));
        patronymic = ContactUtils.getUTF8String(request.getParameter("patronymic"));
        age1 = Integer.parseInt(request.getParameter("age1"));
        age2 = Integer.parseInt(request.getParameter("age2"));
        gender = Integer.parseInt(request.getParameter("gender"));
        citizenship = ContactUtils.getUTF8String(request.getParameter("citizenship"));
        relationship = Integer.parseInt(request.getParameter("relationship"));
        companyName = ContactUtils.getUTF8String(request.getParameter("companyName"));
        country = Integer.parseInt(request.getParameter("country"));
        city = Integer.parseInt(request.getParameter("city"));
        street = ContactUtils.getUTF8String(request.getParameter("street"));
        postcode = ContactUtils.getUTF8String(request.getParameter("postcode"));
    }

    private void setRequestAttributes(HttpServletRequest request) {
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("patronymic", patronymic);
        request.setAttribute("age1", age1);
        request.setAttribute("age2", age2);
        request.setAttribute("gender", gender);
        request.setAttribute("citizenship", citizenship);
        request.setAttribute("relationship", relationship);
        request.setAttribute("companyName", companyName);
        request.setAttribute("country", country);
        request.setAttribute("city", city);
        request.setAttribute("street", street);
        request.setAttribute("postcode", postcode);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "search";

        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            parseRequest(request);
            setRequestAttributes(request);
            RelationshipDao rshDao = new RelationshipDaoImpl(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new CityDaoImpl(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);

            ContactDao contactDao = new ContactDaoImpl(connection);
            List<Contact> contactList = contactDao.find(firstName, lastName, patronymic, age1, age2, gender, citizenship, relationship, companyName, country, city, street, postcode);
            request.setAttribute("contactList", contactList);

        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            throw new CommandExecutionException("error while connecting to database", e);
        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            throw new CommandExecutionException("error while closing database connection", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("encoding is not supported", e);
            throw new CommandExecutionException("error while parsing parameters encoding", e);
        }

        return VIEW_NAME;
    }
}
