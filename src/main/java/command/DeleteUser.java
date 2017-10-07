package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteUser implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteUser.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactService contactService = new ContactServiceImpl(connection);
        UserService userService = new UserServiceImpl(connection);
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        List<Contact> contactList = null;
        List<Attachment> attachmentList = null;
        try {
            String userLogin = request.getUserPrincipal().getName();
            contactList = contactService.getByLoginUser(userLogin);
            List<Integer> contactIdList = contactList.stream().map(Contact::getId).collect(Collectors.toList());
            attachmentList = attachmentService.getByContactIdIn(contactIdList);

//            cascade deletion contacts and attachments and phones
            userService.delete(userLogin);
            RequestUtils.setMessageText(request, "Учетная запись успешно удалена", TooltipType.success);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        contactList.forEach(contactService::deleteProfileImageFile);

        if (attachmentList != null)
            attachmentList.forEach(attachmentService::deleteAttachmentFile);

        return new Logout().execute(request, response, connection);
    }
}
