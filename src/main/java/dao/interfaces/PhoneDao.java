package dao.interfaces;

import model.Phone;

import java.util.List;

/**
 * Created by maxim on 25.09.2016.
 */
public interface PhoneDao {
    List<Phone> getPhoneByContactID(int id);
}
