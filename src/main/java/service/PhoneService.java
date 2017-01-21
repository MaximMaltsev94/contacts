package service;

import exceptions.DaoException;
import model.Phone;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PhoneService {
    List<Phone> parseRequest(HttpServletRequest request, int contactId);

    void insert(Phone phone) throws DaoException;
    void insert(List<Phone> phoneList) throws DaoException;
    List<Phone> getByContactID(int contactID) throws DaoException;
    void deleteByContactID(int contactID) throws DaoException;

}
