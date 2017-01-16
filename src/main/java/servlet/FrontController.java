package servlet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import command.Command;
import command.CommandFactory;
import command.RequestHandler;
import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TooltipType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;

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
            CommandFactory commandFactory = new CommandFactory();
            Command command = commandFactory.getCommand(request);

            if(command == null) {
                throw new DataNotFoundException("command not found for method " + request.getMethod() + " and action " + request.getParameter("action"));
            }

            viewName = command.execute(request, response);

        } catch (DataNotFoundException e) {
            LOG.error("", e);
            request.getSession().setAttribute("tooltip-type", TooltipType.danger.toString());
            request.getSession().setAttribute("tooltip-text", "Произведено неизвестное действие");
            viewName = null;
        } catch (CommandExecutionException e) {
            LOG.error("");
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
