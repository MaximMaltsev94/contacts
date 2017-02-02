package service;

import exceptions.DaoException;
import model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAll() throws DaoException;
    Country getByID(int countyID) throws DaoException;
}
