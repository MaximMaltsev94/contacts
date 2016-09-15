package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MySqlConnectionFactory implements ConnectionFactory{
    private static volatile MySqlConnectionFactory instance;
    private static BasicDataSource dataSource;

    private static void fillDSProperties() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("db");

            String drvName = bundle.getString("driverClassName");
            String Url = bundle.getString("databaseURL");
            String Username = bundle.getString("user");
            String Password = bundle.getString("password");
            String InitialSize = bundle.getString("poolSize");
        }catch (Exception e) {
            e.printStackTrace();
        }

//        try (FileInputStream fis = new FileInputStream("src/main/resources/db.property")) {
//            Properties properties = new Properties();
//            properties.load(fis);
//            dataSource.setDriverClassName(properties.getProperty("driverClassName"));
//            dataSource.setUrl(properties.getProperty("databaseURL"));
//            dataSource.setUsername(properties.getProperty("user"));
//            dataSource.setPassword(properties.getProperty("password"));
//            dataSource.setInitialSize(Integer.parseInt(properties.getProperty("poolSize")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static MySqlConnectionFactory getInstance() {
        MySqlConnectionFactory localInstance = instance;
        if(localInstance == null) {
            synchronized (MySqlConnectionFactory.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new MySqlConnectionFactory();
                    dataSource = new BasicDataSource();
                    fillDSProperties();
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
