package service;

import dao.implementation.CityDaoImpl;
import dao.interfaces.CityDao;
import exceptions.DaoException;
import model.City;

import java.sql.Connection;
import java.util.List;

public class CityServiceImpl implements CityService {
    private Connection connection;

    public CityServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<City> getAll() throws DaoException {
        CityDao cityDao = new CityDaoImpl(connection);
        return cityDao.getAll();
    }

    @Override
    public City getByID(int cityID) throws DaoException {
        CityDao cityDao = new CityDaoImpl(connection);
        return cityDao.getByID(cityID);
    }
}
