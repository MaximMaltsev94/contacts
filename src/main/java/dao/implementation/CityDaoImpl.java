package dao.implementation;

import dao.interfaces.CityDao;
import exceptions.DaoException;
import model.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityDaoImpl implements CityDao {
    private static final Logger LOG = LoggerFactory.getLogger(CityDaoImpl.class);
    private Connection connection;

    public CityDaoImpl(Connection connection) {
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
    public List<City> getAll() throws DaoException {
        List<City> cityList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `city`");
            ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                City city = parseResultSet(rs);
                cityList.add(city);
            }
        } catch (SQLException e) {
            LOG.error("can't get city list", e);
            throw new DaoException("error while getting city list", e);
        }
        return cityList;
    }

    private PreparedStatement createGetByIDStatement(int cityID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `city` WHERE `id` = ?");
        preparedStatement.setObject(1, cityID);
        return preparedStatement;
    }

    @Override
    public City getByID(int cityID) throws DaoException {
        City city = null;
        try(PreparedStatement preparedStatement = createGetByIDStatement(cityID);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                city = parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("can't get city by id - {}", cityID, e);
            throw new DaoException("error while getting city", e);
        }
        return city;
    }

    @Override
    public List<City> getByIDIn(List<Integer> idList) throws DaoException {
        List<City> cityList = new ArrayList<>();
        try(PreparedStatement preparedStatement = DaoUtils.createDynamicWhereInSQL(connection, "SELECT * FROM `city` WHERE `id` in (", "", idList, 1);
            ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                cityList.add(parseResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.error("can't get city by id list - {}", idList, e);
            throw new DaoException("error while getting city list", e);
        }
        return cityList;
    }
}
