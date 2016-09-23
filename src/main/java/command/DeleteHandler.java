package command;

import dao.interfaces.ContactDao;
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

/**
 * Created by maxim on 21.09.2016.
 */
public class DeleteHandler implements RequestHandler {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteHandler.class);

    private void deleteProfileImage(String uploadPath, int id) throws NamingException {
        ContactDao contactDao = new MySqlContactDao();
        Contact contact = contactDao.getByID(id);
        String imageUrl = contact.getProfilePicture();
        if(imageUrl.contains("pri")) {
            String imageName = imageUrl.substring(imageUrl.indexOf("pri"));
            new File(uploadPath + imageName).delete();
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int contactID = Integer.parseInt(request.getParameter("id"));
            ContactDao contactDao = new MySqlContactDao();
            deleteProfileImage(request.getServletContext().getInitParameter("uploadPath"), contactID);
            contactDao.deleteByID(contactID);
            response.sendRedirect("/contact/?action=show");
        }catch (NamingException e) {
            LOG.warn("can't get db connection", e);
            response.sendRedirect("/contact/?action=show");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
