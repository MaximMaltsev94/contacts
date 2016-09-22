package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by maxim on 14.09.2016.
 */
public interface RequestHandler {
    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
