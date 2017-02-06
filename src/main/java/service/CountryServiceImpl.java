package service;

import dao.implementation.CountryDaoImpl;
import dao.interfaces.CountryDao;
import exceptions.DaoException;
import model.Country;

import java.sql.Connection;
import java.util.List;

public class CountryServiceImpl implements CountryService {

    private CountryDao countryDao;

    public CountryServiceImpl(Connection connection) {
        countryDao = new CountryDaoImpl(connection);
    }

    @Override
    public List<Country> getAll() throws DaoException {
        return countryDao.getAll();
    }

    @Override
    public Country getByID(int countyID) throws DaoException {
        return countryDao.getByID(countyID);
    }

    @Override
    public List<Country> getByIDIn(List<Integer> idList) throws DaoException {
        return countryDao.getByIDIn(idList);
    }
}
