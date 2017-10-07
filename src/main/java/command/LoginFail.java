package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class LoginFail implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        RequestUtils.setMessageText(request, "Неверное имя пользователя или пароль", TooltipType.danger);
        request.getSession().setAttribute("j_username", request.getParameter("j_username"));
        return null;
    }
}
