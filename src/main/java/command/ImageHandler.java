package command;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by maxim on 21.09.2016.
 */
public class ImageHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(ImageHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/contact/?action=show&page=1");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String imageName = request.getParameter("name");
            response.setContentType("image/png");

            String uploadPath = request.getServletContext().getInitParameter("uploadPath");
            File f = new File(uploadPath + imageName);
            BufferedImage bi = ImageIO.read(f);
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "png", out);
            out.close();
        } catch (IOException e) {
            LOG.warn("can't find image - ", request.getParameter("name"), e);
        }
    }
}
