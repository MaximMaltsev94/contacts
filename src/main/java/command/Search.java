package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

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
        int CONTACTS_PER_PAGE = 10;

        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);
        ContactService contactService = new ContactServiceImpl(connection);

        try {
            ContactSearchCriteria searchCriteria = new ContactSearchCriteria(request);

            int pageNumber = Integer.parseInt((String) request.getAttribute("page"));

            Page<Contact> contactPage = contactService.getByLoginUser(searchCriteria, pageNumber, CONTACTS_PER_PAGE, request.getUserPrincipal().getName());
            long maxPageNumber = (long)Math.ceil((double) contactPage.getTotalRowCount() / (double) CONTACTS_PER_PAGE);

            maxPageNumber = Math.max(maxPageNumber, 1);

            if (pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }

            setRequestAttributes(request, searchCriteria);

            request.setAttribute("relationshipList", relationshipService.getAll());
            request.setAttribute("countryList", countryService.getAll());

            request.setAttribute("contactList", contactPage.getData());
            request.setAttribute("maxPageNumber", maxPageNumber);

        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return VIEW_NAME;
    }
}
