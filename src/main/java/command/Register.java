package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.User;
import model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserRolesService;
import service.UserRolesServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import util.RequestUtils;
import util.TooltipType;

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

        try {
            User user = userService.parseRequest(request);
            if (userService.getByLogin(user.getLogin()) != null) {
                RequestUtils.setMessageText(request, "Пользователь с таким логином уже существует", TooltipType.danger);
            } else {
                connection.setAutoCommit(false);
                userService.insert(user);

                UserRoles userRoles = new UserRoles();
                userRoles.setRoleName("user");
                userRoles.setLogin(user.getLogin());

                userRolesService.insert(userRoles);

                connection.commit();
                connection.setAutoCommit(true);

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
        }
        return null;
    }
}
