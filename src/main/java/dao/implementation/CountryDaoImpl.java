package dao.implementation;

import dao.interfaces.CountryDao;
import exceptions.DaoException;
import model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDaoImpl implements CountryDao {
    private static final Logger LOG = LoggerFactory.getLogger(CountryDaoImpl.class);
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

    @Override
    public List<Country> getByIDIn(List<Integer> idList) throws DaoException {
        List<Country> countryList = new ArrayList<>();
        try (PreparedStatement preparedStatement = DaoUtils.createDynamicWhereInSQL(connection, "SELECT * FROM `country` WHERE `id` in (", "", idList, 1);
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
