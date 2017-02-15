package command;

import exceptions.CommandExecutionException;
import exceptions.DataNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactFileUtils;

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
            String imageName = (String) request.getAttribute("name");
            if(StringUtils.isBlank(imageName)) {
                LOG.error("image name not specified in request attributes");
                throw new DataNotFoundException("image name not specified in request attributes");
            }
            File f = new File(ContactFileUtils.getSystemFilePath(imageName));

            response.setContentType("image/png");
            BufferedImage bi = ImageIO.read(f);
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "png", out);
            out.close();
        } catch (IOException e) {
            LOG.error("can't find image - ", request.getAttribute("name"), e);
            throw new DataNotFoundException("requested image is not found", e);
        }
        return null;
    }
}
