package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import command.Command;
import command.CommandFactory;
import dao.implementation.ConnectionFactory;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DataNotFoundException;
import exceptions.RequestMapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private String getParametersString(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getAttributeNames();
        Map<String, Object> parameterMap = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, request.getAttribute(name));
        }

        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            result = objectMapper.writeValueAsString(parameterMap);
        } catch (IOException e) {
            LOG.error("error while converting request parameters to json", e);
        }
        return result;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {


        String viewName = null;

        try(Connection connection = ConnectionFactory.getInstance().getConnection()) {
            RequestMapper mapper = new RequestMapper();
            mapper.mapRequestParamsToAttributes(request);

            LOG.info("Received {} request - {} {}", request.getMethod(),
                    request.getRequestURL().toString(),
                    getParametersString(request));

            CommandFactory commandFactory = new CommandFactory();
            Command command = commandFactory.getCommand(request);


            if(command == null) {
                throw new DataNotFoundException("command not found for method " + request.getMethod() + " and action " + request.getAttribute("action"));
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

//                response.sendRedirect("?action=show&page=" + request.getSession().getAttribute("lastVisitedPage"));
                response.sendRedirect("?action=show&page=1");
            } else {
                request.getRequestDispatcher("/WEB-INF/view/" + viewName + ".jsp").forward(request, response);
            }
        } catch (ServletException | IOException e) {
            LOG.error("error while redirect/forward request", e);
        }
    }
}
