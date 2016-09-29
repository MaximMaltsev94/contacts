package command;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Student on 9/29/2016.
 */
public class DocumentHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(DocumentHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fileName = "file" + StringUtils.substringAfter(request.getParameter("name"), "file");
            File file = new File(request.getServletContext().getInitParameter("uploadPath") + fileName);

            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
        } catch (IOException e) {
            LOG.warn("can't find file - ", request.getParameter("name"), e);
        }
    }
}
