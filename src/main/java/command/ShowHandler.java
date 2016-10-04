package command;

import dao.interfaces.ConnectionFactory;
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
import java.util.List;

public class ShowHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(ShowHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("?action=show&page=1");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = MySqlConnectionFactory.getInstance().getConnection();
            int pageNumber = Integer.parseInt(request.getParameter("page"));
            ContactDao contactDao = new MySqlContactDao(connection);
            int rowsCount = contactDao.getRowsCount();
            int maxPageNumber = (rowsCount / 10) + (rowsCount % 10 == 0 ? 0 : 1);
            if(maxPageNumber == 0) {
                maxPageNumber = 1;
            }
            if (pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }
            List<Contact> contactList = contactDao.getContactsPage(pageNumber);
            request.setAttribute("contactList", contactList);
            request.setAttribute("maxPageNumber", maxPageNumber);
            request.getSession().setAttribute("lastVisitedPage", pageNumber);
        } catch (NumberFormatException ex) {
            LOG.warn("incorrect page number {}", request.getParameter("page"), ex);
            response.sendRedirect("?action=show&page=1");
            return;
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        } finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {}
        }
        request.getRequestDispatcher("/WEB-INF/view/contacts.jsp").forward(request, response);
    }
}
