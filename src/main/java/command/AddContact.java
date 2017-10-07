package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import exceptions.RequestParseException;
import model.Attachment;
import model.Contact;
import model.ContactGroups;
import model.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class AddContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(AddContact.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        PhoneService phoneService = new PhoneServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

        Contact contact = null;
        boolean isErrorOccurred = true;
        try{
            contact = contactService.parseRequest(request);
            contact = contactService.insert(contact);


            List<Attachment> attachmentList = attachmentService.parseRequest(request, contact.getId());
            List<Phone> phoneList = phoneService.parseRequest(request, contact.getId());
            List<ContactGroups> contactGroupsList = contactGroupsService.parseRequest(request, contact.getId());

            attachmentService.insert(attachmentList);
            phoneService.insert(phoneList);
            contactGroupsService.insert(contactGroupsList);

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
