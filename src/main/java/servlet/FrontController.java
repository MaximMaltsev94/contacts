package servlet;

import command.Command;
import command.CommandFactory;
import dao.implementation.ConnectionFactory;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DataNotFoundException;
import exceptions.RequestMapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.UserServiceImpl;
import util.RequestMapper;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private ConnectionFactory connectionFactory;
    private CommandFactory commandFactory;
    private RequestMapper requestMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connectionFactory = ConnectionFactory.getInstance();
        commandFactory = new CommandFactory();
        requestMapper = new RequestMapper();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String viewName = null;

        try(Connection connection = connectionFactory.getConnection()) {
            requestMapper.mapRequestParamsToAttributes(request);

            UserService userService = new UserServiceImpl(connection);
            if(request.getUserPrincipal() != null) {
                request.setAttribute("user", userService.getByLogin(request.getUserPrincipal().getName()));
            }

            LOG.info("Received {} request - {} {}", request.getMethod(),
                    request.getRequestURL().toString(),
                    RequestUtils.getParametersString(request));

            Command command = commandFactory.getCommand(request);


            if(command == null) {
                throw new DataNotFoundException("command not found for method " + request.getMethod() + " and path " + request.getPathInfo());
            }

            viewName = command.execute(request, response, connection);

        } catch (DataNotFoundException e) {
            LOG.error("", e);
            RequestUtils.setMessageText(request, "Произведено неизвестное действие", TooltipType.danger);
            viewName = null;
        } catch (CommandExecutionException e) {
            LOG.error("some problems during command execution", e);
            viewName = "error";
        } catch (RequestMapperException e) {
            LOG.error("error while mapping request parameters to request attributes");
            viewName = "error";
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            viewName = "error";
        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            viewName = "error";
        } catch (Exception e) {
            LOG.error("some other problem", e);
            viewName = "error";
        }

        try {
            if(viewName == null) {
//                response.sendRedirect("?action=show&page=1");
                response.sendRedirect(request.getContextPath() + "/contact/show?page=1");
            } else {
                request.getRequestDispatcher("/WEB-INF/view/" + viewName + ".jsp").forward(request, response);
            }
        } catch (ServletException | IOException e) {
            LOG.error("error while redirect/forward request", e);
        }
    }
}
