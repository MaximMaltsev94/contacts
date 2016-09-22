package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.CountryDao;
import model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public class MySqlCountryDao implements CountryDao {
    private final static Logger LOG = LoggerFactory.getLogger(MySqlCountryDao.class);
    private ConnectionFactory connectionFactory;

    public MySqlCountryDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public List<Country> getAll() {
        List<Country> countryList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`country`");
            ResultSet rs = preparedStatement.executeQuery();) {

            while (rs.next()) {
                Country c = new Country();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhoneCode(rs.getInt("phone_code"));
                countryList.add(c);
            }
        } catch (SQLException e) {
            LOG.warn("can't read country list", e);
        }
        return countryList;
    }
}
