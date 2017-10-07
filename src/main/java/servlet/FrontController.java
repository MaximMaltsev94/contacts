package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import command.Command;
import command.CommandFactory;
import dao.implementation.ConnectionFactory;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DataNotFoundException;
import exceptions.RequestParamHandlerException;
import service.UserService;
import service.UserServiceImpl;
import util.request.MultipartRequestParamHandler;
import util.request.RequestParamHandler;
import util.request.RequestParamsToAttibutesHandler;
import util.request.RequestUtils;
import util.request.TooltipType;

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private transient ConnectionFactory connectionFactory;
    private transient CommandFactory commandFactory;
    private transient List<RequestParamHandler> requestParamHandlers;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            connectionFactory = ConnectionFactory.getInstance();
            commandFactory = new CommandFactory();
            requestParamHandlers = new ArrayList<>();
            requestParamHandlers.add(new RequestParamsToAttibutesHandler());
            requestParamHandlers.add(new MultipartRequestParamHandler());
        } catch (IOException e) {
            LOG.error("cannot read properties file", e);
        }
    }
    
    private void handleRequestParams(HttpServletRequest request) throws RequestParamHandlerException {
        for(RequestParamHandler handler : requestParamHandlers) {
            handler.handleRequestParams(request);
        }
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
            handleRequestParams(request);

            UserService userService = new UserServiceImpl(connection);
            if(request.getUserPrincipal() != null) {
                request.setAttribute("user", userService.getByLogin(request.getUserPrincipal().getName()));
            }

            LOG.info("Received {} request - {} {}", request.getMethod(),
                    request.getRequestURL(),
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
        } catch (RequestParamHandlerException e) {
            LOG.error("error while mapping request parameters to request attributes", e);
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
                if(!response.isCommitted()) {
//                response.sendRedirect("?action=show&page=1");
                    response.sendRedirect(request.getContextPath() + "/contact/show?page=1");
                }
            } else {
                request.getRequestDispatcher("/WEB-INF/view/" + viewName + ".jsp").forward(request, response);
            }
        } catch (ServletException | IOException e) {
            LOG.error("error while redirect/forward request", e);
        }
    }
}
