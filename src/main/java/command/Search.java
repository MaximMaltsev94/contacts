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
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Search implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(Search.class);

    private void setRequestAttributes(HttpServletRequest request, ContactSearchCriteria searchCriteria) {
        request.setAttribute("firstName", searchCriteria.getFirstName());
        request.setAttribute("lastName", searchCriteria.getLastName());
        request.setAttribute("patronymic", searchCriteria.getPatronymic());
        request.setAttribute("age1", searchCriteria.getAge1());
        request.setAttribute("age2", searchCriteria.getAge2());
        request.setAttribute("gender", searchCriteria.getGender());
        request.setAttribute("citizenship", searchCriteria.getCitizenship());
        request.setAttribute("relationship", searchCriteria.getRelationship());
        request.setAttribute("companyName", searchCriteria.getCompanyName());
        request.setAttribute("country", searchCriteria.getCountry());
        request.setAttribute("city", searchCriteria.getCity());
        request.setAttribute("street", searchCriteria.getStreet());
        request.setAttribute("postcode", searchCriteria.getPostcode());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "search";

        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);
        CityService cityService = new CityServiceImpl(connection);
        ContactService contactService = new ContactServiceImpl(connection);

        try {
            ContactSearchCriteria searchCriteria = new ContactSearchCriteria(request);

            setRequestAttributes(request, searchCriteria);

            request.setAttribute("relationshipList", relationshipService.getAll());
            request.setAttribute("countryList", countryService.getAll());
            request.setAttribute("cityList", cityService.getAll());

            List<Contact> contactList = contactService.get(searchCriteria, 1, 10);
            request.setAttribute("contactList", contactList);

        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return VIEW_NAME;
    }
}
