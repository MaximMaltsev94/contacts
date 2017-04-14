package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.ContactGroups;
import model.UserGroups;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CreateList implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(CreateList.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);

        try {
            String loginUser = request.getUserPrincipal().getName();
            String groupName = (String)request.getAttribute("groupName");

            UserGroups group = userGroupsService.insert(new UserGroups(groupName, loginUser));

            List<ContactGroups> contactGroupsList = contactGroupsService.parseRequest(request, group.getId(), true);
            contactGroupsService.insert(contactGroupsList);

            RequestUtils.setMessageText(request, "Список " + groupName + " успешно создан.", TooltipType.success);
        } catch (DaoException e) {
            LOG.error("can't insert group", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
