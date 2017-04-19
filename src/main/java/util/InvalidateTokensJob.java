package util;

import dao.implementation.ConnectionFactory;
import exceptions.ConnectionException;
import exceptions.DaoException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserResetTokensService;
import service.UserResetTokensServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class InvalidateTokensJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(InvalidateTokensJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try(Connection connection = ConnectionFactory.getInstance().getConnection()) {
            LOG.info("start removing invalid tokens");
            UserResetTokensService userResetTokensService = new UserResetTokensServiceImpl(connection);
            userResetTokensService.removeInvalidTokens();
        } catch (SQLException e ) {
            LOG.error("can't close connection to database", e);
        }  catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
        } catch (DaoException e) {
            LOG.error("error while accessing to database", e);
        }
    }
}
