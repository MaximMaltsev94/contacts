package command;

import exceptions.CommandExecutionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactFileUtils;
import util.ResponseFileWriter.ResponseFileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public abstract class GetFileTemplate implements Command {
    private static final Logger LOG = LoggerFactory.getLogger(GetFileTemplate.class);

    protected File getFile(HttpServletRequest request, Connection connection) throws DataNotFoundException, IOException, DaoException {
        String fileName = (String) request.getAttribute("name");
        if(StringUtils.isBlank(fileName)) {
            LOG.error("file name not specified in request attributes");
            throw new DataNotFoundException("image name not specified in request attributes");
        }

        return new File(ContactFileUtils.getSystemFilePath(fileName));
    }

    protected void postProcessFile(File file) {

    }

    protected abstract ResponseFileWriter getResponseFileWriter(Connection connection, File file) throws DaoException;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        try{
            File file = getFile(request, connection);
            ResponseFileWriter responseFileWriter = getResponseFileWriter(connection, file);
            responseFileWriter.writeFileToResponse(request, response);
            postProcessFile(file);
        } catch (IOException e) {
            LOG.error("can't find file - ", request.getAttribute("name"), e);
            throw new DataNotFoundException("requested image is not found", e);
        } catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database", e);
        }
        return null;
    }
}
