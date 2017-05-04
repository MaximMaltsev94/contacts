package command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;
import service.VKService;
import service.VKServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class GetImportVkPart implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetImportVkPart.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try {
            response.setContentType("application/javascript");

            VKService vkService = new VKServiceImpl();
            ContactService contactService = new ContactServiceImpl(connection);

            int pageNumber = Integer.parseInt((String)request.getAttribute("vkPage"));
            UserActor userActor = (UserActor) request.getSession().getAttribute("userActor");
            try {
                List<UserXtrLists> friendsList = vkService.getFriendsPart(userActor, pageNumber, 10);
                List<Contact> contactList = contactService.mapVkFriendToContact(friendsList, request.getUserPrincipal().getName());

                String callback = String.format("%s(%s)", request.getAttribute("callback"), new ObjectMapper().writeValueAsString(contactList));
                response.getWriter().write(callback);
            } catch (JsonProcessingException e) {
                LOG.error("error while converting list to json", e);
                response.getWriter().write("{error: 'Ошибка при обработке обработке данных.'}");
            }  catch (ApiException | ClientException e) {
                LOG.error("error while getting data from vk", e);
                response.getWriter().write("{error: 'Ошибка при получении данных ВКонтакте.'}");
            }
        }catch (IOException e) {
            LOG.info("can't write data to response", e);
        } finally {
            try {
                response.getWriter().flush();
            } catch (IOException e) {
                LOG.info("can't write data to response", e);
            }
        }

        return null;
    }
}
