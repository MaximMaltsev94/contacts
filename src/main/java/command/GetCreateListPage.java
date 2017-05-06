package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class GetCreateListPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetCreateListPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "editList";
        ContactService contactService = new ContactServiceImpl(connection);

        try {
            String userLogin = request.getUserPrincipal().getName();
            request.setAttribute("contactList", contactService.getByLoginUser(userLogin));
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        request.setAttribute("action", "createList");
        return VIEW_PAGE;
    }
}
