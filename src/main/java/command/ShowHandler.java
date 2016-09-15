package command;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Created by maxim on 14.09.2016.
 */
public class ShowHandler implements RequestHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNumber = 1;
        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ContactDao contactDao = new MySqlContactDao();
        List<Contact> contactList = contactDao.getContactsPage(pageNumber);
        request.setAttribute("contactList", contactList);
        request.getRequestDispatcher("/WEB-INF/view/contacts.jsp").forward(request, response);
    }
}
