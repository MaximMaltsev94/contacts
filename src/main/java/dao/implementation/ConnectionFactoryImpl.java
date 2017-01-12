package dao.implementation;

import dao.interfaces.ConnectionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactoryImpl implements ConnectionFactory{
    private static volatile ConnectionFactoryImpl instance;
    private static DataSource dataSource;

    private static void configureDataSource() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        dataSource = (DataSource) envContext.lookup("jdbc/contactsDB_maltsev");
    }

    public static ConnectionFactoryImpl getInstance() throws NamingException {
        ConnectionFactoryImpl localInstance = instance;
        if(localInstance == null) {
            synchronized (ConnectionFactoryImpl.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new ConnectionFactoryImpl();
                    configureDataSource();
                }
            }
        }
        return localInstance;
    }


    private ConnectionFactoryImpl() {
    }

    @Override
    synchronized public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
