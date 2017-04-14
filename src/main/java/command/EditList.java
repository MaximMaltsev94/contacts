package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.ContactGroups;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactGroupsService;
import service.ContactGroupsServiceImpl;
import service.UserGroupsService;
import service.UserGroupsServiceImpl;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class EditList implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(EditList.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

        try {
            String loginUser = request.getUserPrincipal().getName();
            String groupName = (String)request.getAttribute("groupName");
            int groupId = Integer.parseInt((String) request.getAttribute("id"));

            UserGroups forUpdate = userGroupsService.getByIdAndLoginUser(groupId, loginUser);
            forUpdate.setGroupName(groupName);

            userGroupsService.update(forUpdate);

            List<ContactGroups> contactGroupsList = contactGroupsService.parseRequest(request, groupId, true);
            contactGroupsService.deleteByGroupId(groupId);
            contactGroupsService.insert(contactGroupsList);

            RequestUtils.setMessageText(request, "Список " + groupName + " успешно редактирован.", TooltipType.success);
        } catch (DaoException e) {
            LOG.error("can't insert group", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
