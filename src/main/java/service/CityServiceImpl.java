package service;

import dao.implementation.CityDaoImpl;
import dao.interfaces.CityDao;
import exceptions.DaoException;
import model.City;

import java.sql.Connection;
import java.util.List;

public class CityServiceImpl implements CityService {

    private CityDao cityDao;

    public CityServiceImpl(Connection connection) {
        cityDao = new CityDaoImpl(connection);
    }

    @Override
    public List<City> getAll() throws DaoException {
        return cityDao.getAll();
    }

    @Override
    public City getByID(int cityID) throws DaoException {
        return cityDao.getByID(cityID);
    }

    @Override
    public List<City> getByIDIn(List<Integer> idList) throws DaoException {
        return cityDao.getByIDIn(idList);
    }
}
