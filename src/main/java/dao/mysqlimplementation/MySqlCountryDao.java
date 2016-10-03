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
    private Connection connection;

    public MySqlCountryDao(Connection connection) {
        this.connection = connection;
    }

    private Country parseResultSet(ResultSet rs) throws SQLException {
        Country c = new Country();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setPhoneCode(rs.getInt("phone_code"));
        return c;
    }

    @Override
    public List<Country> getAll() {
        List<Country> countryList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`country`");
             ResultSet rs = preparedStatement.executeQuery();) {

            while (rs.next()) {
                Country country = parseResultSet(rs);
                countryList.add(country);
            }
        } catch (SQLException e) {
            LOG.warn("can't read country list", e);
        }
        return countryList;
    }

    private PreparedStatement createGetByIDStatement(int contactID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`country` WHERE `id` = ?");
        preparedStatement.setObject(1, contactID);
        return preparedStatement;
    }

    @Override
    public Country getByID(int countryID) {
        Country country = null;
        try (PreparedStatement preparedStatement = createGetByIDStatement(countryID);
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                country = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.warn("can't read country by id - {}", countryID, e);
        }
        return country;
    }
}
