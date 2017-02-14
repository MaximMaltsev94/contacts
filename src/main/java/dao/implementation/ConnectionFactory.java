package dao.implementation;

import exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
    private final static Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
    private static final ConnectionFactory instance;

    static {
        try {
            instance = new ConnectionFactory();
        } catch (ConnectionException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static DataSource dataSource;

    public static ConnectionFactory getInstance() {
        return instance;
    }

    private ConnectionFactory() throws ConnectionException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/contactsDB");
        } catch (NamingException e) {
            LOG.error("can't find datasource in environment context", e);
            throw new ConnectionException("error while finding datasource in environment context", e);
        }
    }

    synchronized public Connection getConnection() throws ConnectionException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOG.error("can't get connection to database", e);
            throw new ConnectionException("error while getting connection to database", e);
        }
    }
}
