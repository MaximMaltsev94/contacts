package servlet;

import command.RequestHandler;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FrontController extends HttpServlet {

    private RequestHandler getHandlerClass(HttpServletRequest request) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String commandName = request.getPathInfo().substring(1).toLowerCase();
        String className = commandName.substring(0, 1).toUpperCase() + commandName.substring(1);
        Class cl = Class.forName("command." + className + "Handler");
        return (RequestHandler) cl.newInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RequestHandler handler = getHandlerClass(request);
            handler.handle(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
