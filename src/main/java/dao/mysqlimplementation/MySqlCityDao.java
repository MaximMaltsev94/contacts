package dao.mysqlimplementation;

import dao.interfaces.CityDao;
import dao.interfaces.ConnectionFactory;
import model.City;
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
public class MySqlCityDao implements CityDao {
    private final static Logger LOG = LoggerFactory.getLogger(MySqlCityDao.class);
    private Connection connection;

    public MySqlCityDao(Connection connection) {
        this.connection = connection;
    }

    private City parseResultSet(ResultSet rs) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("id"));
        city.setName(rs.getString("name"));
        city.setCountryID(rs.getInt("id_country"));
        return city;
    }

    @Override
    public List<City> getAll() {
        List<City> cityList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`city`");
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                City city = parseResultSet(rs);
                cityList.add(city);
            }
        } catch (SQLException e) {
            LOG.warn("can't get city list", e);
        }
        return cityList;
    }

    private PreparedStatement createGetByIDStatement(int cityID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`city` WHERE `id` = ?");
        preparedStatement.setObject(1, cityID);
        return preparedStatement;
    }

    @Override
    public City getByID(int cityID) {
        City city = null;
        try(PreparedStatement preparedStatement = createGetByIDStatement(cityID);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                city = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.warn("can't get city by id - ", cityID, e);
        }
        return city;
    }
}
