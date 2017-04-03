package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class Logout implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(Logout.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try {
            request.logout();
        } catch (ServletException e) {
            LOG.error("error while executing logout method", e);
            throw new CommandExecutionException("can't log out", e);
        }
        return null;
    }
}
