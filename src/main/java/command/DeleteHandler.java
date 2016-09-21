package command;

import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by maxim on 21.09.2016.
 */
public class DeleteHandler implements RequestHandler {

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
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase("post")) {
            try {
                int contactID = Integer.parseInt(request.getParameter("id"));
                ContactDao contactDao = new MySqlContactDao();
                deleteProfileImage(request.getServletContext().getInitParameter("uploadPath"), contactID);
                contactDao.deleteByID(contactID);
                response.sendRedirect("/contact/?action=show");
            }catch (Exception ex) {
                ex.printStackTrace();
                response.sendRedirect("/contact/?action=show");
            }
        }
    }
}
