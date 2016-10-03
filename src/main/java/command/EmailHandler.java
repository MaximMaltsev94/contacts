package command;
import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 03.10.2016.
 */
public class EmailHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(EmailHandler.class);
    private void processRequest(HttpServletRequest request, HttpServletResponse response, List<Integer> contactIds) throws ServletException, IOException {
        try(Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
            ContactDao contactDao = new MySqlContactDao(connection);
            List<Contact> contactsWithEmail = contactDao.getContactsWithEmail();

            request.setAttribute("contactList", contactsWithEmail);
            request.setAttribute("selectedContacts", contactIds);
            request.getRequestDispatcher("/WEB-INF/view/email.jsp").forward(request, response);
        } catch (SQLException | NamingException e) {
            LOG.warn("can't get db connection", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] splitedIDs = request.getParameter("id").split(",");
        List<Integer> contactIds = new ArrayList<>();
        for (String id : splitedIDs) {
            contactIds.add(Integer.parseInt(id));
        }
        processRequest(request, response, contactIds);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, new ArrayList<>());
    }
}
