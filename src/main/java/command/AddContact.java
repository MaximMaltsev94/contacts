package command;

import exceptions.*;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

public class AddContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(AddContact.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        Contact contact = null;
        boolean isErrorOccurred = true;
        try{
            contact = contactService.parseRequest(request);
            contactService.insert(contact);

            RequestUtils.setMessageText(request, "Контакт " + contact.getFirstName() + " " + contact.getLastName() + " успешно сохранен", TooltipType.success);
            isErrorOccurred = false;
        } catch (DaoException e) {
            LOG.error("can't insert contact - {} to database", contact, e);
            throw new CommandExecutionException("error while inserting contacts to database", e);
        } catch (RequestParseException e) {
            LOG.error("can't parse contact info", e);
            throw new CommandExecutionException("error while parsing request", e);
        } finally {
            if(isErrorOccurred && contact != null) {
                LOG.info("deleting profile image from file system - {}", contact.getProfilePicture());
                contactService.deleteProfileImageFile(contact);
            }
        }

        return null;
    }
}
