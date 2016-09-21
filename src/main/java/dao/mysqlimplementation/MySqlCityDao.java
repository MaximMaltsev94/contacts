package dao.mysqlimplementation;

import dao.interfaces.CityDao;
import dao.interfaces.ConnectionFactory;
import model.City;

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
    private ConnectionFactory connectionFactory;

    public MySqlCityDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public List<City> getAll() {
        List<City> cityList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`city`")) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                City city = new City();
                city.setId(rs.getInt("id"));
                city.setName(rs.getString("name"));
                city.setCountryID(rs.getInt("id_country"));
                cityList.add(city);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }
}
