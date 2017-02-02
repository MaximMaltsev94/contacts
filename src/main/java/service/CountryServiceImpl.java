package service;

import dao.implementation.CountryDaoImpl;
import dao.interfaces.CountryDao;
import exceptions.DaoException;
import model.Country;

import java.sql.Connection;
import java.util.List;

public class CountryServiceImpl implements CountryService {
    private Connection connection;

    public CountryServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Country> getAll() throws DaoException {
        CountryDao countryDao = new CountryDaoImpl(connection);
        return countryDao.getAll();
    }

    @Override
    public Country getByID(int countyID) throws DaoException {
        CountryDao countryDao = new CountryDaoImpl(connection);
        return countryDao.getByID(countyID);
    }
}
