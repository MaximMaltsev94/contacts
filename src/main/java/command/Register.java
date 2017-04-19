package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.User;
import model.UserGroups;
import model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

public class Register implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(Register.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {

        UserService userService = new UserServiceImpl(connection);
        UserRolesService userRolesService = new UserRolesServiceImpl(connection);
        UserGroupsService userGroupsService = new UserGroupsServiceImpl(connection);

        try {
            User user = userService.parseRequest(request);
            if (userService.getByLogin(user.getLogin()) != null) {
                RequestUtils.setMessageText(request, "Пользователь с таким логином уже существует", TooltipType.danger);
            } else if(userService.getByEmail(user.getEmail()) != null) {
                RequestUtils.setMessageText(request, "Пользователь с таким email уже существует", TooltipType.danger);
            } else {
                connection.setAutoCommit(false);
                userService.insert(user);

                userRolesService.insert(new UserRoles(user.getLogin(), "user"));

                userGroupsService.insert(new UserGroups("Друзья", user.getLogin()));
                userGroupsService.insert(new UserGroups("Семья", user.getLogin()));
                userGroupsService.insert(new UserGroups("Коллеги", user.getLogin()));

                connection.commit();
                connection.setAutoCommit(true);

                request.login(user.getLogin(), (String) request.getAttribute("password"));

                RequestUtils.setMessageText(request, "Регистрация пользователя " + user.getLogin() + " успешно завершена", TooltipType.success);
            }
        } catch (SQLException | DaoException e) {
            LOG.error("error while accessing to database", e);
            if(connection != null) {
                LOG.info("rolling back transaction");
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOG.error("can't rollback transaction");
                }
            }
            throw new CommandExecutionException("error while accessing database", e);
        } catch (ServletException e) {
            LOG.error("error while executing login method", e);
            throw new CommandExecutionException("can't log out", e);
        }
        return null;
    }
}
