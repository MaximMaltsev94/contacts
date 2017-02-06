package service;

import exceptions.DaoException;
import model.City;

import java.util.List;

public interface CityService {
    List<City> getAll() throws DaoException;
    City getByID(int cityID) throws DaoException;
    List<City> getByIDIn(List<Integer> idList) throws DaoException;
}
