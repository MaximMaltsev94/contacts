package command;

import exceptions.DaoException;
import util.ResponseFileWriter.ResponseFileWriterImpl;

import java.io.File;
import java.sql.Connection;

public class GetImage extends GetFileTemplate {
    @Override
    protected ResponseFileWriterImpl getResponseFileWriter(Connection connection, File file) throws DaoException {
        return new ResponseFileWriterImpl(file);
    }
}
