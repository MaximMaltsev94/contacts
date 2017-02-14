package servlet;

import dao.implementation.ConnectionFactory;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.RequestMapperException;
import model.User;
import model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserRolesService;
import service.UserRolesServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import util.RequestMapper;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private final static Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMapper requestMapper = new RequestMapper();

        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            requestMapper.mapRequestParamsToAttributes(request);
            UserService userService = new UserServiceImpl(connection);
            UserRolesService userRolesService = new UserRolesServiceImpl(connection);

            User user = userService.parseRequest(request);
            if(userService.getByLogin(user.getLogin()) != null) {
                RequestUtils.setMessageText(request, "Пользователь с таким логином уже существует", TooltipType.danger);
            } else {
                connection.setAutoCommit(false);
                userService.insert(user);

                UserRoles userRoles = new UserRoles();
                userRoles.setRoleName("user");
                userRoles.setLogin(user.getLogin());

                userRolesService.insert(userRoles);

                connection.commit();
                connection.setAutoCommit(false);

                RequestUtils.setMessageText(request, "Регистрация успешно завершена", TooltipType.success);
            }
            response.sendRedirect("/contact");
            return;
        } catch (RequestMapperException e) {
            LOG.error("can't map request params to attributes");
//
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
//            viewName = "error";
        } catch (DaoException | SQLException e) {
            LOG.error("error while accessing to database", e);
            if(connection != null) {
                LOG.info("rolling back transaction");
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    LOG.error("can't rollback transaction");
                }
            }
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("can't close connection to database");
                }
            }
        }
        request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/contact/");
    }
}
