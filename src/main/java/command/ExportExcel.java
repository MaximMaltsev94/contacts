package command;

import exceptions.DaoException;
import model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;
import util.ResponseFileWriter.DownloadResponseFileWriter;
import util.ResponseFileWriter.ResponseFileWriter;
import util.ResponseFileWriter.ResponseFileWriterImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class ExportExcel extends GetFileTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(ExportExcel.class);

    @Override
    protected ResponseFileWriter getResponseFileWriter(Connection connection, File file) throws DaoException {
        return new DownloadResponseFileWriter(new ResponseFileWriterImpl(file), "contacts.xls");
    }

    @Override
    protected File getFile(HttpServletRequest request, Connection connection) throws IOException, DaoException {
        ContactService contactService = new ContactServiceImpl(connection);
        List<Contact> contactList = contactService.getByLoginUser(request.getUserPrincipal().getName());
        return contactService.writeContactsToExcel(contactList);
    }

    @Override
    protected void postProcessFile(File file) {
        file.delete();
    }
}
