package dao.interfaces;

import model.Country;

import java.util.List;

/**
 * Created by maxim on 20.09.2016.
 */
public interface CountryDao {
    List<Country> getAll();
}
