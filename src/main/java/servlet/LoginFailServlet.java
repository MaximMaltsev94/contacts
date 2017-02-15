package servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtils;
import util.TooltipType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailServlet extends HttpServlet {
    private final static Logger LOG = LoggerFactory.getLogger(LoginFailServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestUtils.setMessageText(request, "Неверное имя пользователя или пароль", TooltipType.danger);
        request.getSession().setAttribute("j_username", request.getParameter("j_username"));
        response.sendRedirect("/contact");
    }
}
