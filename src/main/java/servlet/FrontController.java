package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.CommandFactory;
import command.RequestHandler;
import exceptions.CommandExecutionException;
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

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private String getParametersString(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";
        try {
            result = objectMapper.writeValueAsString(request.getParameterMap());
        } catch (IOException e) {
            LOG.error("error while converting request parameters to json");
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

        LOG.info("Received {} request - {} {}", request.getMethod(),
                                                request.getRequestURL().toString(),
                                                getParametersString(request));

        String viewName = null;

        try {
            RequestMapper mapper = new RequestMapper();
            mapper.mapRequestParamsToAttributes(request);

            CommandFactory commandFactory = new CommandFactory();
            Command command = commandFactory.getCommand(request);

            if(command == null) {
                throw new DataNotFoundException("command not found for method " + request.getMethod() + " and action " + request.getParameter("action"));
            }

            viewName = command.execute(request, response);

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
