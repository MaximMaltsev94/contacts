package service;

import exceptions.DaoException;
import model.City;

import java.util.List;

public interface CityService {
    List<City> getByIDIn(List<Integer> idList) throws DaoException;
}
