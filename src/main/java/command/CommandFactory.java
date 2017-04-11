package command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final static Logger LOG = LoggerFactory.getLogger(CommandFactory.class);
    private Map<String, Class<? extends Command>> commands;

    public CommandFactory() {
        commands = new HashMap<>();
        commands.put("get/show", GetContactListPage.class);
        commands.put("get/image", GetImage.class);
        commands.put("get/document", GetDocument.class);

        commands.put("get/add", GetAddContactPage.class);
        commands.put("post/add", AddContact.class);

        commands.put("get/edit", GetEditPage.class);
        commands.put("post/edit", EditContact.class);

        commands.put("post/delete", DeleteContact.class);

        commands.put("get/search", GetSearchPage.class);
        commands.put("post/search", Search.class);

        commands.put("get/email", GetEmailPage.class);
        commands.put("post/email", GetEmailPage.class);

        commands.put("post/submitemail", SendEmail.class);

        commands.put("get/login", Login.class);
        commands.put("post/register", Register.class);
        commands.put("get/logout", Logout.class);
        commands.put("get/loginfail", LoginFail.class);
        commands.put("post/loginfail", LoginFail.class);

        commands.put("get/user", GetUserPage.class);
        commands.put("post/user", UpdateUser.class);
        commands.put("post/deleteuser", DeleteUser.class);
    }

    public Command getCommand(HttpServletRequest request) {
        String action = request.getPathInfo();
        if(StringUtils.isBlank(action) || action.equals("/")) {
            action = "/show";
            request.setAttribute("page", "1");
        }
        String commandKey = request.getMethod() + action;
        Class<? extends Command> commandClass = commands.get(commandKey.toLowerCase());

        if(commandClass == null) {
            return null;
        }

        Command command = null;
        try {
            command = commandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("can't instantiate class", e);
        }
        return command;
    }
}
