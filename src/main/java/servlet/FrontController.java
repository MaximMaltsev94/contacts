package servlet;

import command.RequestHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends HttpServlet {

    private final static Logger LOG = LoggerFactory.getLogger(FrontController.class);

    private RequestHandler getHandlerClass(HttpServletRequest request) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String commandName = request.getParameter("action").toLowerCase();

        String className = StringUtils.capitalize(commandName);
        Class cl = Class.forName("command." + className + "Handler");
        return (RequestHandler) cl.newInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("Received get request - {}{}", request.getRequestURL().toString(), request.getQueryString());
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        } catch (Exception e) {
            LOG.debug("unknown action - {}", request.getParameter("action"), e);
            response.sendRedirect("/contact/?action=show&page=1");
        }

    }
}
