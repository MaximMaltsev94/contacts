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
import java.util.List;

public class ShowHandler implements RequestHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pageNumber = Integer.parseInt(request.getParameter("page"));
            ContactDao contactDao = new MySqlContactDao();
            List<Contact> contactList = contactDao.getContactsPage(pageNumber);
            request.setAttribute("contactList", contactList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        request.getRequestDispatcher("/WEB-INF/view/contacts.jsp").forward(request, response);
    }
}
