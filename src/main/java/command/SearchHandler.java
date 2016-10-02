package command;

import dao.interfaces.*;
import dao.mysqlimplementation.*;
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
        firstName = request.getParameter("firstName");
        lastName = request.getParameter("lastName");
        patronymic = request.getParameter("patronymic");
        try {
            age1 = Integer.parseInt(request.getParameter("age1"));
        }catch (NumberFormatException e) {
            age1 = 0;
        }

        try {
            age2 = Integer.parseInt(request.getParameter("age2"));
        } catch (NumberFormatException e) {
            age2 = 0;
        }
        try {
            gender = Integer.parseInt(request.getParameter("gender"));
        }catch (NumberFormatException e) {
            gender = 2;
        }
        citizenship = request.getParameter("citizenship");
        try {
            relationship = Integer.parseInt(request.getParameter("relationship"));
        } catch (NumberFormatException e) {
            relationship = 0;
        }
        companyName = request.getParameter("companyName");

        try {
            country = Integer.parseInt(request.getParameter("country"));
        } catch (NumberFormatException e) {
            country = 0;
        }
        try {
            city = Integer.parseInt(request.getParameter("city"));
        } catch (NumberFormatException e) {
            city = 0;
        }
        street = request.getParameter("street");
        postcode = request.getParameter("postcode");
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
            parseRequest(request);
            setRequestAttributes(request);
            RelationshipDao rshDao = new MySqlRelationshipDao(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new MySqlCountryDao(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new MySqlCityDao(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);
            request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        }
    }
}
