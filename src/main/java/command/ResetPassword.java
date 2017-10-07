package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.User;
import model.UserResetTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserResetTokensService;
import service.UserResetTokensServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import util.request.RequestUtils;
import util.request.TooltipType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ResetPassword implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(ResetPassword.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {

        UserResetTokensService userResetTokensService = new UserResetTokensServiceImpl(connection);
        UserService userService = new UserServiceImpl(connection);

        try {
            String token = (String) request.getAttribute("token");
            String password = (String) request.getAttribute("password");
            UserResetTokens userResetTokens = userResetTokensService.get(token);
            if(userResetTokens == null) {
                RequestUtils.setMessageText(request, "Невалидный токен для восстановления пароля", TooltipType.danger);
                return null;
            }

            User user = userService.getByLogin(userResetTokens.getLogin());
            userService.setHashedPassword(user, password);
            userService.update(user);

            RequestUtils.setMessageText(request, "Пароль успешно обновлен", TooltipType.success);
            if(request.getUserPrincipal() == null)
                request.login(user.getLogin(), password);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        } catch (ServletException e) {
            LOG.error("error while login", e);
            throw new CommandExecutionException("can't login", e);
        }
        return null;
    }
}
