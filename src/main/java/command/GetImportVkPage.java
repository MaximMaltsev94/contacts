package command;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.queries.users.UserField;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.VKService;
import service.VKServiceImpl;
import util.ContactUtils;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GetImportVkPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetImportVkPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "editList";

        try {
            UserActor actor = (UserActor) request.getSession().getAttribute("userActor");
            if(actor == null) {
                return new ShowOauthPage().execute(request, response, connection);
            }

            VKService vkService = new VKServiceImpl();
            List<Contact> contactList = vkService.getFriendsPart(actor, 1, 10, request.getUserPrincipal().getName());

            request.setAttribute("action", "importVK");
            request.getSession().setAttribute("friendList", contactList);
            request.setAttribute("contactList", contactList.stream().map(e -> {
                Contact contact = new Contact();
                contact.setId(e.getId());
                contact.setFirstName(e.getFirstName());
                contact.setLastName(e.getLastName());
                contact.setProfilePicture(e.getProfilePicture());
                return contact;
            }).collect(Collectors.toList()));

        } catch (ApiException | ClientException e) {
            LOG.error("can't access vk", e);
            RequestUtils.setMessageText(request, "Произошла ошибка при получении данных Вконтакте", TooltipType.danger);
            return null;
        }

        return VIEW_NAME;
    }
}
