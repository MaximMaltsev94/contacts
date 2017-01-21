package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

public class GetImage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetImage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
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
            LOG.error("can't find image - ", request.getParameter("name"), e);
            throw new DataNotFoundException("requested image is not found", e);
        }
        return null;
    }
}
