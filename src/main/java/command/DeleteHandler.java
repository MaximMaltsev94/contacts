package command;

import dao.interfaces.AttachmentDao;
import dao.interfaces.ContactDao;
import dao.implementation.AttachmentDaoImpl;
import dao.implementation.ConnectionFactoryImpl;
import dao.implementation.ContactDaoImpl;
import model.Attachment;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by maxim on 21.09.2016.
 */
public class DeleteHandler implements RequestHandler {

    private final static Logger LOG = LoggerFactory.getLogger(DeleteHandler.class);

    private void deleteProfileImage(Connection connection, String uploadPath, int contactId) throws NamingException {
        ContactDao contactDao = new ContactDaoImpl(connection);
        Contact contact = contactDao.getByID(contactId);
        ContactUtils.deleteFileByUrl(contact.getProfilePicture(), uploadPath, "pri");
    }

    private void deleteAttachments(Connection connection, String uploadPath, int contactId) {
        AttachmentDao attachmentDao = new AttachmentDaoImpl(connection);
        List<Attachment> attachmentList = attachmentDao.getByContactId(contactId);
        for (Attachment attachment : attachmentList) {
            ContactUtils.deleteFileByUrl(attachment.getFilePath(), uploadPath, "file");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (Connection connection = ConnectionFactoryImpl.getInstance().getConnection()) {
            String[] splitedIDs = request.getParameter("id").split(",");
            for (String id : splitedIDs) {
                int contactID = Integer.parseInt(id);
                String uploadPath = request.getServletContext().getInitParameter("uploadPath");
                deleteProfileImage(connection, uploadPath, contactID);
                deleteAttachments(connection, uploadPath, contactID);

                ContactDao contactDao = new ContactDaoImpl(connection);
                contactDao.deleteByID(contactID);
            }
            response.sendRedirect("?action=show&page=" + request.getSession().getAttribute("lastVisitedPage"));
        } catch (NamingException | SQLException e) {
            LOG.warn("can't get db connection", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("?action=show&page=1");
    }
}
