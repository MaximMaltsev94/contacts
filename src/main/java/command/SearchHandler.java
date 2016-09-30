package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by maxim on 30.09.2016.
 */
public class SearchHandler implements RequestHandler {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
    }
}
