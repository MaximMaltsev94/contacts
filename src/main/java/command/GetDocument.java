package command;

import exceptions.DaoException;
import model.Attachment;
import org.apache.commons.io.FilenameUtils;
import service.AttachmentService;
import service.AttachmentServiceImpl;
import util.ContactUtils;
import util.ResponseFileWriter.DownloadResponseFileWriter;
import util.ResponseFileWriter.ResponseFileWriter;
import util.ResponseFileWriter.ResponseFileWriterImpl;

import java.io.File;
import java.sql.Connection;

public class GetDocument extends GetFileTemplate {
    private String getFileNameForDownloadWindow(Connection connection, String fileName) throws DaoException {
        AttachmentService attachmentService = new AttachmentServiceImpl(connection);
        Attachment attachment = attachmentService.getByFilePath(fileName);

        return ContactUtils.cyr2lat(attachment.getFileName()) + "." + FilenameUtils.getExtension(fileName);
    }

    @Override
    protected ResponseFileWriter getResponseFileWriter(Connection connection, File file) throws DaoException {
        String dialogFileName = getFileNameForDownloadWindow(connection, file.getName());
        return new DownloadResponseFileWriter(new ResponseFileWriterImpl(file), dialogFileName);
    }
}
