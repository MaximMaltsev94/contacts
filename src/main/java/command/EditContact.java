package command;

import dao.implementation.ConnectionFactory;
import exceptions.*;
import model.Attachment;
import model.Contact;
import model.Phone;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.ContactUtils;
import util.RequestUtils;
import util.TooltipType;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(EditContact.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {

        ContactService contactService = new ContactServiceImpl(connection);
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        PhoneService phoneService = new PhoneServiceImpl(connection);

        Contact newContact = null;
        Contact oldContact = null;

        List<Attachment> newAttachments = null;
        List<Attachment> oldAttachments = null;


        List<Attachment> forDelete = null;
        List<Attachment> forUpdate = null;
        List<Attachment> forInsert = null;

        boolean isErrorOccurred = true;

        try {
            connection.setAutoCommit(false);

            newContact = contactService.parseRequest(request);
            int contactId = Integer.parseInt((String)request.getAttribute("id"));
            oldContact = contactService.getByID(contactId);
            if(oldContact == null) {
                throw new DataNotFoundException("contact with given id - " + contactId + " is not found");
            }


            newContact.setId(contactId);
            contactService.update(newContact);

            List<Phone> phones = phoneService.parseRequest(request, contactId);
            phoneService.deleteByContactID(contactId);
            phoneService.insert(phones);


            oldAttachments = attachmentService.getByContactId(contactId);
            newAttachments = attachmentService.parseRequest(request, contactId);

            List<Integer> newAttachmentIDS = newAttachments.stream()
                    .map(Attachment::getId)
                    .collect(Collectors.toList());

            forDelete = oldAttachments.stream()
                    .filter(oldAttachment -> !newAttachmentIDS.contains(oldAttachment.getId()))
                    .collect(Collectors.toList());

            forUpdate = oldAttachments.stream()
                    .filter(oldAttachment -> newAttachmentIDS.contains(oldAttachment.getId()))
                    .collect(Collectors.toList());

            for (Attachment forUpdateAttachment : forUpdate) {
                for (Attachment newAttachment : newAttachments) {
                    if(newAttachment.getId() == forUpdateAttachment.getId()) {
                        forUpdateAttachment.setComment(newAttachment.getComment());
                        forUpdateAttachment.setFileName(newAttachment.getFileName());
                    }
                }
            }


            forInsert = newAttachments.stream()
                    .filter(newAttachment -> newAttachment.getId() == 0)
                    .collect(Collectors.toList());


            attachmentService.delete(forDelete, false);
            attachmentService.insert(forInsert);
            attachmentService.update(forUpdate);

            connection.commit();
            connection.setAutoCommit(false);

            RequestUtils.setMessageText(request, "Контакт " + newContact.getFirstName() + " " + newContact.getLastName() + " успешно редактирован", TooltipType.success);
            isErrorOccurred = false;

        } catch (SQLException | DaoException e) {
            LOG.error("error while editind transaction", e);
            try {
                if (connection != null) {
                    LOG.info("rolling back transaction ", e);
                    connection.rollback();
                }
            } catch (SQLException e1) {
                LOG.error("error while rolling back transaction ", e1);
            }
            RequestUtils.setMessageText(request, "Произошла ошибка при редактировании. Информация контакта не обновлена", TooltipType.danger);
        } catch (RequestParseException e) {
            LOG.error("can't parse request", e);
            throw new CommandExecutionException("error while parsing request", e);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                LOG.error("error while closing connection ", e);
            }
        }

        if(isErrorOccurred) {
            if(newContact != null) {
                contactService.deleteProfileImageFile(newContact);
            }
            if(newAttachments != null) {
                for (Attachment newAttachment : newAttachments) {
                    attachmentService.deleteAttachmentFile(newAttachment);
                }
            }

        } else {
            contactService.deleteProfileImageFile(oldContact);

            if (forDelete != null) {
                for (Attachment attachment : forDelete) {
                    attachmentService.deleteAttachmentFile(attachment);
                }
            }
        }

        return null;
    }
}
