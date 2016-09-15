package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnectionFactory implements ConnectionFactory{
    private static volatile MySqlConnectionFactory instance;
    private static DataSource dataSource;

    private static void configureDataSource() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:comp/env");
        dataSource = (DataSource) envContext.lookup("jdbc/contactsDB_maltsev");
    }

    public static MySqlConnectionFactory getInstance() throws NamingException {
        MySqlConnectionFactory localInstance = instance;
        if(localInstance == null) {
            synchronized (MySqlConnectionFactory.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new MySqlConnectionFactory();
                    configureDataSource();
                }
            }
        }
        return localInstance;
    }


    private MySqlConnectionFactory() {
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
