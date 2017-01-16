package command;

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
        // TODO: 13.01.2017 add map filling
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
    }

    public Command getCommand(HttpServletRequest request) {
        // TODO: 13.01.2017 change request.getParameter to request.getAttribute
//        String commandKey = request.getMethod() + "/" + request.getAttribute("action");
        String commandKey = request.getMethod() + "/" + request.getParameter("action");
        Class<? extends Command> commandClass = commands.get(commandKey.toLowerCase());

        if(commandClass == null) {
            return null;
        }

        Command command = null;
        try {
            command = commandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("can't instantiate class");
        }
        return command;
    }
}
