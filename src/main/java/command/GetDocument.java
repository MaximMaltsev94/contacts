package command;

import dao.implementation.AttachmentDaoImpl;
import dao.implementation.ConnectionFactory;
import dao.interfaces.AttachmentDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class GetDocument implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetDocument.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try {
            String fileName = "file" + StringUtils.substringAfter(request.getParameter("name"), "file");
            String filePath = request.getServletContext().getInitParameter("uploadPath") + fileName;
            File file = new File(filePath);
            FileInputStream in = new FileInputStream(file);

            String mimeType = request.getServletContext().getMimeType(filePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType + ";charset=UTF-8");
            response.setContentLength((int) file.length());

            // forces download
            String headerKey = "Content-Disposition";
            //--------
            String nameParameter = request.getParameter("name");
            AttachmentDao attachmentDao = new AttachmentDaoImpl(connection);
            Attachment attachment = attachmentDao.getByFilePath(nameParameter);
            //--------

            String str = ContactUtils.cyr2lat(attachment.getFileName()) + "." + FilenameUtils.getExtension(filePath);

            String headerValue = String.format("attachment; filename=\"%s\"", str);
            response.setHeader(headerKey, headerValue);

            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
        } catch (IOException e) {
            LOG.warn("can't find file - {} in filesystem", request.getParameter("name"), e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
