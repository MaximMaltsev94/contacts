package command;

import dao.implementation.ConnectionFactory;
import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetContactListPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetContactListPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "contacts";
        try (Connection connection = ConnectionFactory.getInstance().getConnection();) {

            int pageNumber = Integer.parseInt(request.getParameter("page"));
            ContactDao contactDao = new ContactDaoImpl(connection);
            int rowsCount = contactDao.getRowsCount();
            int maxPageNumber = (rowsCount / 10) + (rowsCount % 10 == 0 ? 0 : 1);

            maxPageNumber = Math.max(maxPageNumber, 1);

            if (pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }

            List<Contact> contactList = contactDao.getContactsPage(pageNumber);
            request.setAttribute("contactList", contactList);
            request.setAttribute("maxPageNumber", maxPageNumber);
            request.getSession().setAttribute("lastVisitedPage", pageNumber);

        } catch (NumberFormatException e) {
            LOG.error("incorrect page number - {}", request.getParameter("page"), e);
            throw new DataNotFoundException("can't find contacts by page number - " + request.getParameter("page"), e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            throw new CommandExecutionException("error while connecting to database", e);
        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            throw new CommandExecutionException("error while closing database connection", e);
        }

        return VIEW_PAGE;
    }
}
