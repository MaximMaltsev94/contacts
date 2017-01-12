package dao.interfaces;

import exceptions.DaoException;
import model.Country;

import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public interface CountryDao {
    List<Country> getAll() throws DaoException;
    Country getByID(int countyID) throws DaoException;
}
