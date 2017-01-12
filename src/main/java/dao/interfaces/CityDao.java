package dao.interfaces;

import exceptions.DaoException;
import model.City;

import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public interface CityDao {
    List<City> getAll() throws DaoException;
    City getByID(int cityID) throws DaoException;
}
