package dao.interfaces;

import exceptions.DaoException;
import model.Phone;

import java.util.List;

/**
 * Created by maxim on 25.09.2016.
 */
public interface PhoneDao {
    List<Phone> getByContactID(int contactID) throws DaoException;
    void deleteByContactID(int contactID) throws DaoException;
    void insert(Phone phone) throws DaoException;
    void insert(List<Phone> phone) throws DaoException;
}
