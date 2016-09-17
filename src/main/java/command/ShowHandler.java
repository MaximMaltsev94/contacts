package command;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ShowHandler implements RequestHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pageNumber = Integer.parseInt(request.getParameter("page"));
            ContactDao contactDao = new MySqlContactDao();
            int rowsCount = contactDao.getRowsCount();
            int maxPageNumber = (rowsCount / 20) + (rowsCount % 20 == 0 ? 0 : 1);
            if(pageNumber > maxPageNumber || pageNumber < 1) {
                throw new NumberFormatException();
            }
            List<Contact> contactList = contactDao.getContactsPage(pageNumber);
            request.setAttribute("contactList", contactList);
            request.setAttribute("maxPageNumber", maxPageNumber);
        } catch (NumberFormatException ex) {
            response.sendRedirect("/contact/?action=show&page=1");
            return;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/WEB-INF/view/contacts.jsp").forward(request, response);
    }
}
