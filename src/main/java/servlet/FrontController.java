package servlet;

import command.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private RequestHandler getHandlerClass(HttpServletRequest request) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String commandName = request.getParameter("action").toLowerCase();

        String className = StringUtils.capitalize(commandName);
        Class cl = Class.forName("command." + className + "Handler");
        return (RequestHandler) cl.newInstance();
    }

    private String getParametersString(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        StringBuilder builder = new StringBuilder();
        for (String key : parameters.keySet()) {
            builder.append(key);
            builder.append(" = ");
            builder.append(request.getParameter(key));
            builder.append(";");
        }
        return builder.toString();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Received post request - {}{}{}", request.getRequestURL().toString(), request.getQueryString(), getParametersString(request));
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Received get request - {}{}___{}", request.getRequestURL().toString(), request.getQueryString(), getParametersString(request));
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            RequestHandler handler = getHandlerClass(request);

            //change to reflection
            switch (request.getMethod().toLowerCase()) {
                case "get":
                    handler.doGet(request, response);
                    break;
                case "post":
                    handler.doPost(request, response);
                    break;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOG.debug("unknown action - {}", request.getParameter("action"), e);
            try {
                response.sendRedirect("?action=show&page=1");
            } catch (IOException e1) {

            }
        } catch (Exception e) {
            LOG.warn("exception ", e);
            try {
                response.sendRedirect("?action=show&page=1");
            } catch (IOException e1) {

            }
        }

    }
}
