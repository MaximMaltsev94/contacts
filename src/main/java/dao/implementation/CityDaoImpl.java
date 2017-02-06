package dao.implementation;

import dao.interfaces.CityDao;
import exceptions.DaoException;
import model.City;
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
public class CityDaoImpl implements CityDao {
    private final static Logger LOG = LoggerFactory.getLogger(CityDaoImpl.class);
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

    private String createGetByIdInSQL(int size) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM `city` WHERE `id` in (");
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
    public List<City> getByIDIn(List<Integer> idList) throws DaoException {
        List<City> cityList = new ArrayList<>();
        try(PreparedStatement preparedStatement = createGetByIdInStatement(connection, idList);
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
