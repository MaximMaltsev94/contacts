package dao.interfaces;

import model.Phone;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by maxim on 25.09.2016.
 */
public interface PhoneDao {
    List<Phone> getPhoneByContactID(int id);
    void deleteByContactID(int contactID) throws SQLException;
    void insert(Phone phone) throws SQLException;
}
