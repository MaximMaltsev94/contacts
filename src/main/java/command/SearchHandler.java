package command;

import dao.interfaces.*;
import dao.implementation.*;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by maxim on 30.09.2016.
 */
public class SearchHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(SearchHandler.class);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = ConnectionFactoryImpl.getInstance().getConnection()) {
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

            request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = ConnectionFactoryImpl.getInstance().getConnection()) {
            RelationshipDao rshDao = new RelationshipDaoImpl(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new CityDaoImpl(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);

            request.setAttribute("gender", 2);
            request.setAttribute("age1", 0);
            request.setAttribute("age2", 0);
            request.setAttribute("country", 0);
            request.setAttribute("city", 0);
            request.setAttribute("relationship", 0);
            request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        }
    }
}
