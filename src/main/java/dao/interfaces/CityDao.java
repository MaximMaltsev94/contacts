package dao.interfaces;

import model.City;

import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public interface CityDao {
    List<City> getAll();
    City getByID(int cityID);
}
