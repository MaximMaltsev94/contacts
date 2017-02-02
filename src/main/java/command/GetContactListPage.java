package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class GetContactListPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetContactListPage.class);

    private final int CONTACTS_PER_PAGE = 10;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "contacts";

        ContactService contactService = new ContactServiceImpl(connection);

        try {

            int pageNumber = Integer.parseInt((String) request.getAttribute("page"));
            long rowsCount = contactService.getCount();
            long maxPageNumber = (long)Math.ceil((double) rowsCount / (double) CONTACTS_PER_PAGE);

            maxPageNumber = Math.max(maxPageNumber, 1);

            if (pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }

            List<Contact> contactList = contactService.get(pageNumber, CONTACTS_PER_PAGE);
            request.setAttribute("contactList", contactList);
            request.setAttribute("maxPageNumber", maxPageNumber);
            request.getSession().setAttribute("lastVisitedPage", pageNumber);

        } catch (NumberFormatException e) {
            LOG.error("incorrect page number - {}", request.getAttribute("page"), e);
            throw new DataNotFoundException("can't find contacts by page number - " + request.getAttribute("page"), e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return VIEW_PAGE;
    }
}
