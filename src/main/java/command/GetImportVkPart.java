package command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.VKService;
import service.VKServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class GetImportVkPart implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetImportVkPart.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        response.setContentType("application/javascript");

        VKService vkService = new VKServiceImpl();

        int pageNumber = Integer.parseInt((String)request.getAttribute("vkPage"));
        UserActor userActor = (UserActor) request.getSession().getAttribute("userActor");
        try {
            List<Contact> contactList = vkService.getFriendsPart(userActor, pageNumber, 10, request.getUserPrincipal().getName());
            String callback = String.format("%s(%s)", request.getAttribute("callback"), new ObjectMapper().writeValueAsString(contactList));
            response.getWriter().write(callback);
            response.getWriter().flush();
        } catch (Exception e) {
        }

        return null;
    }
}
