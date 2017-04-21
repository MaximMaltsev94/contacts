package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetEditPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetEditPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "editContact";

        ContactService contactService = new ContactServiceImpl(connection);
        RelationshipService relationshipService = new RelationshipServiceImpl(connection);
        CountryService countryService = new CountryServiceImpl(connection);
        PhoneService phoneService = new PhoneServiceImpl(connection);
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

        try {
            int contactID = Integer.parseInt(request.getParameter("id"));
            long maxID = contactService.getMaxID();
            if (contactID < 1 || contactID > maxID) {
                throw new NumberFormatException("id out of range");
            }

            String loginUser = request.getUserPrincipal().getName();
            Contact contact = contactService.getByIDAndLoginUser(contactID, loginUser);
            if(contact == null) {
                throw new DataNotFoundException("contact with id - " + contactID + " and loginUser - " + loginUser + " not found");
            }

            request.setAttribute("contact", contact);
            request.setAttribute("relationshipList", relationshipService.getAll());
            request.setAttribute("countryList", countryService.getAll());
            request.setAttribute("phoneList", phoneService.getByContactID(contactID));
            request.setAttribute("attachmentList", attachmentService.getByContactId(contactID));
            request.setAttribute("userGroups", userGroupsService.getByLogin(loginUser));

            Set<Integer> contactGroupsIdList = contactGroupsService.getByContactId(contactID).stream().map(ContactGroups::getGroupId).collect(Collectors.toSet());
            request.setAttribute("contactGroups", contactGroupsIdList);

            request.setAttribute("action", "edit");

        } catch (NumberFormatException e) {
            LOG.warn("incorrect contact id {}", request.getParameter("id"), e);
            throw new DataNotFoundException("can't find contact with specified id", e);
        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_NAME;
    }
}
