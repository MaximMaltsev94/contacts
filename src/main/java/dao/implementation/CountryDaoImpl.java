package dao.implementation;

import dao.interfaces.CountryDao;
import exceptions.DaoException;
import model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public class CountryDaoImpl implements CountryDao {
    private final static Logger LOG = LoggerFactory.getLogger(CountryDaoImpl.class);
    private Connection connection;

    public CountryDaoImpl(Connection connection) {
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
    public List<Country> getAll() throws DaoException {
        List<Country> countryList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `country`");
             ResultSet rs = preparedStatement.executeQuery();) {

            while (rs.next()) {
                Country country = parseResultSet(rs);
                countryList.add(country);
            }
        } catch (SQLException e) {
            LOG.error("can't read country list", e);
            throw new DaoException("error while getting countries list", e);
        }
        return countryList;
    }

    private PreparedStatement createGetByIDStatement(int contactID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `country` WHERE `id` = ?");
        preparedStatement.setObject(1, contactID);
        return preparedStatement;
    }

    @Override
    public Country getByID(int countryID) throws DaoException {
        Country country = null;
        try (PreparedStatement preparedStatement = createGetByIDStatement(countryID);
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                country = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't read country by id - {}", countryID, e);
            throw new DaoException("error while getting country by ID", e);
        }
        return country;
    }

    private String createGetByIdInSQL(int size) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `country` WHERE `id` in (");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

    private PreparedStatement createGetByIdInStatement(Connection connection, List<Integer> idList) throws SQLException {
        String sql = createGetByIdInSQL(idList.size());
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int i = 1; i <= idList.size(); i++) {
            statement.setObject(i, idList.get(i - 1));
        }

        return statement;
    }

    @Override
    public List<Country> getByIDIn(List<Integer> idList) throws DaoException {
        List<Country> countryList = new ArrayList<>();
        try (PreparedStatement preparedStatement = createGetByIdInStatement(connection, idList);
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                countryList.add(parseResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.error("can't get countries by id list - {}", idList, e);
            throw new DaoException("error while getting country by ID list", e);
        }
        return countryList;
    }
}
