package command;

import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by maxim on 21.09.2016.
 */
public class DeleteHandler implements RequestHandler {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteHandler.class);

    private void deleteProfileImage(String uploadPath, int id) throws NamingException {
        Connection connection = null;
        try {
            connection = MySqlConnectionFactory.getInstance().getConnection();
            ContactDao contactDao = new MySqlContactDao(connection);
            Contact contact = contactDao.getByID(id);
            String imageUrl = contact.getProfilePicture();
            if (imageUrl.contains("pri")) {
                String imageName = imageUrl.substring(imageUrl.indexOf("pri"));
                new File(uploadPath + imageName).delete();
            }
        } catch (SQLException e) {
            LOG.warn("can't get db connection");
        } finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {}
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        try {
            connection = MySqlConnectionFactory.getInstance().getConnection();
            int contactID = Integer.parseInt(request.getParameter("id"));
            ContactDao contactDao = new MySqlContactDao(connection);
            deleteProfileImage(request.getServletContext().getInitParameter("uploadPath"), contactID);
            contactDao.deleteByID(contactID);
            response.sendRedirect("/contact/?action=show&page=" + request.getSession().getAttribute("lastVisitedPage"));
        }catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
            response.sendRedirect("/contact/?action=show");
        } finally {
            if(connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {}
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
