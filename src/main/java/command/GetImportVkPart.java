package command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.UserFull;
import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetImportVkPart implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetImportVkPart.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try {
            response.setContentType("application/javascript");

            ContactService contactService = new ContactServiceImpl(connection);

            int pageNumber = Integer.parseInt((String)request.getAttribute("vkPage"));
            String loginUser = request.getUserPrincipal().getName();
            UserActor userActor = (UserActor) request.getSession().getAttribute("userActor");
            try {

                List<? extends UserFull> friendsList = contactService.getVkPart(userActor, pageNumber, 10, loginUser);

                List<Contact> contactList = contactService.mapVkFriendToContact(friendsList, loginUser);

                String callback = String.format("%s(%s)", request.getAttribute("callback"), new ObjectMapper().writeValueAsString(contactList));
                response.getWriter().write(callback);
            } catch (JsonProcessingException e) {
                LOG.error("error while converting list to json", e);
                response.getWriter().write(request.getAttribute("callback") + "({error: 'Ошибка при обработке обработке данных.'})");
            }  catch (ApiException | ClientException e) {
                LOG.error("error while getting data from vk", e);
                response.getWriter().write(request.getAttribute("callback") + "({error: 'Ошибка при получении данных ВКонтакте.'})");
            } catch (DaoException e) {
                LOG.error("error while accessing database", e);
                response.getWriter().write(request.getAttribute("callback") + "({error: 'Ошибка при обращении к базе данных.'})");
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
