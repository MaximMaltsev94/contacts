package command;


import dao.interfaces.AttachmentDao;
import dao.mysqlimplementation.MySqlAttachmentDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import model.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactUtils;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Student on 9/29/2016.
 */
public class DocumentHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(DocumentHandler.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("?action=show&page=1");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try (Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
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
            AttachmentDao attachmentDao = new MySqlAttachmentDao(connection);
            Attachment attachment = attachmentDao.findByFilePath(nameParameter);
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
            LOG.warn("can't find file - ", request.getParameter("name"), e);
        } catch (SQLException e) {
            LOG.warn("can't get db connection", e);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
