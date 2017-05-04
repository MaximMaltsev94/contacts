package command;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import model.ContactGroups;
import model.UserGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class ImportVk implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(ImportVk.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        ContactGroupsService contactGroupsService = new ContactGroupsServiceImpl(connection);
        ContactService contactService = new ContactServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);
        UserActor userActor = (UserActor) request.getSession().getAttribute("userActor");
        VKService vkService = new VKServiceImpl(userActor);

        try {
            String groupName = (String) request.getAttribute("groupName");
            String loginUser = request.getUserPrincipal().getName();
            List<UserGroups> userGroupsList = userGroupsService.getByGroupNameAndLogin(groupName, loginUser);
            UserGroups userGroups;
            if(userGroupsList.isEmpty()) {
                userGroups = userGroupsService.insert(new UserGroups(groupName, loginUser));
            } else {
                userGroups = userGroupsList.get(0);
            }

            List<Integer> userIdList = contactGroupsService.parseRequest(request, userGroups.getId(), true).stream().map(ContactGroups::getContactID).collect(Collectors.toList());

            List<? extends UserFull> friends = vkService.getFriendsByIdIn(userIdList);
            List<Contact> contactList = contactService.mapVkFriendToContact(friends, loginUser);
            contactService.saveRemoteImages(contactList);
            List<Integer> generatedIds = contactService.insert(contactList);
            List<ContactGroups> contactGroupsList = generatedIds.stream().map(e -> new ContactGroups(userGroups.getId(), e)).collect(Collectors.toList());
            contactGroupsService.insert(contactGroupsList);

        } catch (DaoException e) {
            LOG.error("can't access database", e);
            throw new CommandExecutionException("error while accessing database", e);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
