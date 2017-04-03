package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Attachment;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AttachmentService;
import service.AttachmentServiceImpl;
import util.ContactFileUtils;
import util.ContactUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

public class GetDocument implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetDocument.class);

    private String getFileNameForDownloadWindow(Connection connection, String fileName) throws DaoException {
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        Attachment attachment = attachmentService.getByFilePath(fileName);

        return ContactUtils.cyr2lat(attachment.getFileName()) + "." + FilenameUtils.getExtension(fileName);
    }

    private void writeFileToResponse(File file, HttpServletResponse response) throws IOException {

        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try {
            String fileName = (String) request.getAttribute("name");
            File file = new File(ContactFileUtils.getSystemFilePath(fileName));

            String mimeType = request.getServletContext().getMimeType(fileName);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType + ";charset=UTF-8");
            response.setContentLength((int)file.length());

            String fileNameForDownloadWindow = getFileNameForDownloadWindow(connection, fileName);

            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileNameForDownloadWindow));

            writeFileToResponse(file, response);

        } catch (IOException e) {
            LOG.warn("can't find file - {} in filesystem",
                    ContactFileUtils.getSystemFilePath((String) request.getAttribute("name")), e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
