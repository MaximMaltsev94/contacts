package dao.implementation;

import dao.interfaces.ConnectionFactory;
import exceptions.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactoryImpl implements ConnectionFactory{
    private final static Logger LOG = LoggerFactory.getLogger(ConnectionFactoryImpl.class);
    private static final ConnectionFactoryImpl instance;

    static {
        try {
            instance = new ConnectionFactoryImpl();
        } catch (ConnectionException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static DataSource dataSource;

    public static ConnectionFactoryImpl getInstance() {
        return instance;
    }

    private ConnectionFactoryImpl() throws ConnectionException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/contactsDB_maltsev");
        } catch (NamingException e) {
            LOG.error("can't find datasource in environment context", e);
            throw new ConnectionException("error while finding datasource in environment context", e);
        }
    }

    @Override
    synchronized public Connection getConnection() throws ConnectionException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOG.error("can't get connection to database", e);
            throw new ConnectionException("error while getting connection to database", e);
        }
    }
}
