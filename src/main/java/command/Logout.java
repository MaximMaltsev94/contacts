package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class Logout implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        HttpSession session=request.getSession();
        session.invalidate();
        return null;
    }
}
