package dao.mysqlimplementation;

import dao.interfaces.ConnectionFactory;
import dao.interfaces.CountryDao;
import model.Country;

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
    private ConnectionFactory connectionFactory;

    public MySqlCountryDao() throws NamingException {
        connectionFactory = MySqlConnectionFactory.getInstance();
    }

    @Override
    public List<Country> getAll() {
        List<Country> countryList = new ArrayList<>();
        try(Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `contacts_maltsev`.`country`")) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Country c = new Country();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhoneCode(rs.getInt("phone_code"));
                countryList.add(c);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }
}
