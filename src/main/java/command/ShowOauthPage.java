package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class ShowOauthPage implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(ShowOauthPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String commandName = StringUtils.substringAfter(request.getRequestURL().toString(), "/contact/");
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("../importVK.properties"));
            String url = String.format("https://oauth.vk.com/authorize?client_id=%s&display=%s&redirect_uri=%s&scope=%s&response_type=%s&v=%s",
                    properties.getProperty("client_id"),
                    properties.getProperty("display"),
                    request.getRequestURL().toString().replace(commandName, properties.getProperty("redirect_uri")),
                    properties.getProperty("scope"),
                    properties.getProperty("response_type"),
                    properties.getProperty("v"));
            response.sendRedirect(url);
        } catch (IOException e) {
            LOG.error("can't redirect to oauth page", e);
        }
        return null;
    }
}
