package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.ContactGroups;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Set;
import java.util.stream.Collectors;

public class GetEditListPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetEditListPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_PAGE = "editList";
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactService contactService = new ContactServiceImpl(connection);

        try {
            String userLogin = request.getUserPrincipal().getName();
            String id = (String) request.getAttribute("id");
            if(id == null) {
                LOG.error("required parameter id not specified");
                throw new DataNotFoundException("required request parameter id not specified");
            }

            int groupId = -1;
            try {
                groupId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                LOG.error("group id - {} is not integer", id);
                throw new DataNotFoundException("can't find user's group with id - " + id, e);
            }

            UserGroups userGroups = userGroupsService.getByIdAndLoginUser(groupId, userLogin);
            if(userGroups == null) {
                LOG.error("can't find group id - {} for user - {}", id, userLogin);
                throw new DataNotFoundException("can't find user's group with id - " + id + " for user " + userLogin);
            }

            Set<Integer> contactIdSet = contactGroupsService.getByGroupId(groupId).stream().map(ContactGroups::getContactID).collect(Collectors.toSet());

            request.setAttribute("contactList", contactService.getByLoginUser(userLogin));
            request.setAttribute("contactGroups", contactIdSet);
            request.setAttribute("groupName", userGroups.getGroupName());
            request.setAttribute("action", "editList");

        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }

        return VIEW_PAGE;
    }
}
