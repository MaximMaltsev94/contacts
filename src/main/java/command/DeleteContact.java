package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AttachmentService;
import service.AttachmentServiceImpl;
import service.ContactService;
import service.ContactServiceImpl;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteContact implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(DeleteContact.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);


        List<Contact> contactList = null;
        List<Attachment> attachmentList = null;

        try {
            connection.setAutoCommit(false);

            List<Integer> idList = Arrays.stream(request.getParameter("id").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            contactList = contactService.getByIdInAndLoginUser(idList, request.getUserPrincipal().getName());
            attachmentList = attachmentService.getByContactIdIn(idList);

//            deleting contact, phones and attachments with cascade deletion
            contactService.delete(idList);

            connection.commit();
            connection.setAutoCommit(true);

            RequestUtils.setMessageText(request, "Выбранные контакты успешно удалены", TooltipType.success);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOG.error("error while rollback transaction");
            }
            throw new CommandExecutionException("error while accessing database",e);
        } catch (SQLException e) {
            LOG.error("handle transaction error", e);
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOG.error("error while rollback transaction");
            }
            throw new CommandExecutionException(e);
        }

//        deleting files from file system if success
        if (contactList != null)
            contactList.forEach(contactService::deleteProfileImageFile);

        if (attachmentList != null)
            attachmentList.forEach(attachmentService::deleteAttachmentFile);

        return null;
    }
}
