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

    @Override
    public List<City> getAll() {
        List<City> cityList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`city`");
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt("id"));
                city.setName(rs.getString("name"));
                city.setCountryID(rs.getInt("id_country"));
                cityList.add(city);
            }
        } catch (SQLException e) {
            LOG.warn("can't get city list", e);
        }
        return cityList;
    }
}
