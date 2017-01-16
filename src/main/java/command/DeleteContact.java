package command;

import dao.implementation.AttachmentDaoImpl;
import dao.implementation.ConnectionFactory;
import dao.implementation.ContactDaoImpl;
import dao.interfaces.AttachmentDao;
import dao.interfaces.ContactDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DeleteContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(DeleteContact.class);

    private void deleteProfileImage(Connection connection, String uploadPath, int contactId) throws DaoException {
        ContactDao contactDao = new ContactDaoImpl(connection);
        Contact contact = contactDao.getByID(contactId);
        ContactUtils.deleteFileByUrl(contact.getProfilePicture(), uploadPath, "pri");
    }

    private void deleteAttachments(Connection connection, String uploadPath, int contactId) throws DaoException {
        AttachmentDao attachmentDao = new AttachmentDaoImpl(connection);
        List<Attachment> attachmentList = attachmentDao.getByContactId(contactId);
        for (Attachment attachment : attachmentList) {
            ContactUtils.deleteFileByUrl(attachment.getFilePath(), uploadPath, "file");
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            String[] splitedIDs = request.getParameter("id").split(",");
            for (String id : splitedIDs) {
                int contactID = Integer.parseInt(id);
                String uploadPath = request.getServletContext().getInitParameter("uploadPath");
                deleteProfileImage(connection, uploadPath, contactID);
                deleteAttachments(connection, uploadPath, contactID);

                ContactDao contactDao = new ContactDaoImpl(connection);
                contactDao.deleteByID(contactID);
            }

            request.getSession().setAttribute("tooltip-type", TooltipType.success.toString());
            request.getSession().setAttribute("tooltip-text", "Выбранные контакты успешно удалены");

        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            throw new CommandExecutionException("error while closing connection to database", e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            throw new CommandExecutionException("error while connecting to database", e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return null;
    }
}
