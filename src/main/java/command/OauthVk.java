package command;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.VkProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class OauthVk implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(OauthVk.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        try {
            VkProperties vkProperties = new VkProperties();
            String code = (String) request.getAttribute("code");
            int appId = vkProperties.getApllicationId();
            String clientSecret = vkProperties.getAppSecret();
            String uri = request.getRequestURL().toString().replace("importVK", vkProperties.getRedirect_uri());
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(appId, clientSecret, uri, code)
                    .execute();

            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
            request.getSession().setAttribute("userActor", actor);
            response.sendRedirect(request.getContextPath() + "/contact/importVK");
        } catch (IOException e) {
            LOG.error("error while redirecting request", e);
        } catch (ApiException | ClientException e) {
            LOG.error("error while authentificate vk");
        }
        return null;
    }
}
